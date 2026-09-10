package dev.biserman.planet

import dev.biserman.planet.gui.Gui
import dev.biserman.planet.gui.Gui.Mode
import dev.biserman.planet.planet.BackgroundTaskProgress
import dev.biserman.planet.planet.Planet
import dev.biserman.planet.planet.PlanetSimulationRunner
import dev.biserman.planet.planet.climate.ClimateSimulation
import dev.biserman.planet.planet.ecology.PlanetEcology
import dev.biserman.planet.planet.tectonics.Erosion
import dev.biserman.planet.planet.tectonics.TectonicGlobals
import dev.biserman.planet.planet.tectonics.Tectonics
import dev.biserman.planet.rendering.PlanetRenderer
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.api.Input
import godot.api.InputEvent
import godot.api.Node
import godot.api.ShaderMaterial
import godot.global.GD
import kotlin.random.Random

@RegisterClass
class Main : Node() {
    private enum class SimulationKind { EDIT, HISTORY }

    private data class PendingPlanetTask(
        val kind: SimulationKind?,
        val name: String,
        val sourceRevision: Long,
        val progress: BackgroundTaskProgress?,
        val commit: (Planet) -> Unit,
    )

    lateinit var planet: Planet private set
    lateinit var planetRenderer: PlanetRenderer
    private val simulationRunner = PlanetSimulationRunner()
    private var pendingPlanetTask: PendingPlanetTask? = null
    private var simulationControlsLocked = false
    private var planetRevision = 0L

    val isSimulationRunning: Boolean
        get() = pendingPlanetTask != null

    fun setStarsEnabled(enabled: Boolean) {
        val worldEnvironment = findChild("WorldEnvironment") as? godot.api.WorldEnvironment ?: return
        val skyMaterial = worldEnvironment.environment?.sky?.skyMaterial as? ShaderMaterial ?: return
        skyMaterial.setShaderParameter("star_density", if (enabled) 400.0 else 0.0)
    }

    var copyElevation: Double? = 0.0

    @RegisterFunction
    override fun _ready() {
        instance = this
        Gui.instance.showSeedSelection()
    }

    fun generatePlanet(seed: Int) {
        val newPlanet = Planet(seed = seed, size = 35)
        GD.print("tiles: ${newPlanet.topology.tiles.size}")
        GD.print("average radius: ${newPlanet.topology.averageRadius}, area: ${newPlanet.topology.averageArea}")

        Tectonics.stepTectonicPlateForces(newPlanet)
        PlanetEcology.activatePlanet(newPlanet)
        planetRenderer = PlanetRenderer(this, newPlanet)

        updatePlanet(newPlanet)
    }

    @RegisterFunction
    override fun _unhandledInput(event: InputEvent?) {
        if (event == null || !::planet.isInitialized || !::planetRenderer.isInitialized) {
            return
        }

        if (Input.isActionJustPressed("next") && !isSimulationRunning) {
            if (Gui.instance.mode == Mode.PLAY) {
                advanceHistoryTurn()
            } else {
                runSelectedEditSimulation()
            }
        }

        if (Gui.instance.mode != Mode.EDIT || isSimulationRunning) return

        val selectedTile = planet.planetTiles[Gui.instance.selectedTile?.id] ?: return
        if (Input.isActionJustPressed("place_land")) {
            selectedTile.elevation = 1.0
            planet.terrainChangeCount++
            planetRenderer.update(planet)
        }

        if (Input.isActionJustPressed("place_ocean")) {
            selectedTile.elevation = -1.0
            planet.terrainChangeCount++
            planetRenderer.update(planet)
        }

        if (Input.isActionJustPressed("paste")) {
            if (copyElevation != null) {
                selectedTile.elevation = copyElevation!!
                planet.terrainChangeCount++
                planetRenderer.update(planet)
            }
        }

        if (Input.isActionJustPressed("copy")) {
            copyElevation = selectedTile.elevation
        }
    }

    private val editSimulationTimerStep = 0.1
    var timerActive = false
        set(value) {
            field = value
            timerTime = editSimulationTimerStep
            unlockSimulationControlsIfIdle()
        }
    private var timerTime = editSimulationTimerStep

    private val historyTurnTimerStep = 0.5
    var historyTimerActive = false
        set(value) {
            field = value
            historyTimerTime = historyTurnTimerStep
            unlockSimulationControlsIfIdle()
        }
    private var historyTimerTime = historyTurnTimerStep

    @RegisterFunction
    override fun _process(delta: Double) {
        updateBackgroundTaskProgress()
        finishPlanetTaskIfReady()

        if (timerActive && Gui.instance.mode == Mode.EDIT && ::planet.isInitialized && ::planetRenderer.isInitialized) {
            timerTime += delta
            if (timerTime >= editSimulationTimerStep) {
                timerTime = 0.0
                runSelectedEditSimulation()
            }
        }

        if (historyTimerActive && Gui.instance.mode == Mode.PLAY && ::planet.isInitialized) {
            historyTimerTime += delta
            if (historyTimerTime >= historyTurnTimerStep) {
                historyTimerTime = 0.0
                advanceHistoryTurn()
            }
        }
    }

    private fun runSelectedEditSimulation() {
        val simulationName = requireNotNull(Gui.instance.selectedSimulation)
        submitSimulation(SimulationKind.EDIT, simulationName, simulations.getValue(simulationName))
    }

    fun advanceHistoryTurn() {
        submitSimulation(SimulationKind.HISTORY, "ecology") { nextPlanet ->
            PlanetEcology.advanceAllOneSeason(nextPlanet)
            nextPlanet.historyTurn++
        }
    }

    private fun submitSimulation(
        kind: SimulationKind,
        name: String,
        simulation: (Planet) -> Unit,
    ) {
        submitPlanetTask(
            kind = kind,
            name = name,
            progress = null,
            task = simulation,
            commit = { refreshAfterSimulation(kind, name) },
        )
    }

    fun submitBackgroundPlanetTask(
        name: String,
        task: (Planet, BackgroundTaskProgress) -> Unit,
        commit: (Planet) -> Unit,
    ) {
        if (timerActive || historyTimerActive) return
        val progress = BackgroundTaskProgress()
        submitPlanetTask(
            kind = null,
            name = name,
            progress = progress,
            task = { workingPlanet -> task(workingPlanet, progress) },
            commit = commit,
        )
    }

    private fun submitPlanetTask(
        kind: SimulationKind?,
        name: String,
        progress: BackgroundTaskProgress?,
        task: (Planet) -> Unit,
        commit: (Planet) -> Unit,
    ) {
        if (isSimulationRunning || !::planet.isInitialized) return
        pendingPlanetTask = PendingPlanetTask(
            kind = kind,
            name = name,
            sourceRevision = planetRevision,
            progress = progress,
            commit = commit,
        )
        simulationRunner.submit(planet, task)
        setSimulationControlsLocked(true)
        if (progress != null) Gui.instance.showBackgroundTaskProgress(name)
    }

    private fun updateBackgroundTaskProgress() {
        val pending = pendingPlanetTask ?: return
        val progress = pending.progress ?: return
        Gui.instance.updateBackgroundTaskProgress(pending.name, progress.snapshot())
    }

    private fun finishPlanetTaskIfReady() {
        val pending = pendingPlanetTask ?: return
        val completion = simulationRunner.poll() ?: return

        pendingPlanetTask = null
        val completedPlanet = when (completion) {
            is PlanetSimulationRunner.Completion.Success -> completion.planet
            is PlanetSimulationRunner.Completion.Failure -> {
                if (pending.progress != null) Gui.instance.hideBackgroundTaskProgress()
                planetTaskFailed(pending, completion.cause)
                unlockSimulationControlsIfIdle()
                return
            }
        }
        if (pending.sourceRevision != planetRevision) {
            if (pending.progress != null) Gui.instance.hideBackgroundTaskProgress()
            unlockSimulationControlsIfIdle()
            return
        }

        check(completedPlanet === planet) { "Simulation completed with an unexpected planet instance" }
        planetRevision++
        if (pending.kind == SimulationKind.HISTORY) {
            // Mutation changes the shared species catalog, so keep this rare
            // commit-time operation on the Godot thread.
            PlanetEcology.mutateAtInterval(planet)
        }
        try {
            pending.commit(planet)
        } catch (cause: Throwable) {
            if (pending.progress != null) Gui.instance.hideBackgroundTaskProgress()
            planetTaskFailed(pending, cause)
            unlockSimulationControlsIfIdle()
            return
        }
        if (pending.progress != null) Gui.instance.hideBackgroundTaskProgress()
        if (pending.kind == null || !isAutoplayActiveFor(pending.kind)) {
            setSimulationControlsLocked(false)
        }
    }

    private fun isAutoplayActiveFor(kind: SimulationKind) = when (kind) {
        SimulationKind.EDIT -> timerActive && Gui.instance.mode == Mode.EDIT
        SimulationKind.HISTORY -> historyTimerActive && Gui.instance.mode == Mode.PLAY
    }

    private fun unlockSimulationControlsIfIdle() {
        if (!timerActive && !historyTimerActive && !isSimulationRunning) {
            setSimulationControlsLocked(false)
        }
    }

    private fun setSimulationControlsLocked(locked: Boolean) {
        if (simulationControlsLocked == locked) return
        simulationControlsLocked = locked
        Gui.instance.setSimulationRunning(locked)
    }

    private fun planetTaskFailed(pending: PendingPlanetTask, cause: Throwable) {
        timerActive = false
        historyTimerActive = false
        Gui.instance.togglePlayButton(false)
        GD.pushError("${pending.name} failed: ${cause.message ?: cause::class.simpleName}")
    }

    private fun refreshAfterSimulation(kind: SimulationKind, name: String) {
        Gui.instance.updateSimulationTimeDisplay()
        planetRenderer.update(planet)
        Gui.instance.statsGraph.update(planet)
        Gui.instance.updateHistoryDisplay()
        Gui.instance.updateInfobox()
        Gui.instance.brushTool.refreshOptions()
        if (kind == SimulationKind.HISTORY) {
            Gui.instance.treeOfLifeView.refresh()
        }
        if (
            name == "tectonics" &&
            TectonicGlobals.tectonicSimulationStop > 0 &&
            planet.tectonicAge % TectonicGlobals.tectonicSimulationStop == 0
        ) {
            Gui.instance.togglePlayButton(false)
        }
    }

    val hasPlanet get() = ::planet.isInitialized

    fun updatePlanet(newPlanet: Planet) {
        GD.print("updating planet: $newPlanet")
        planet = newPlanet
        planetRevision++
        PlanetEcology.activatePlanet(newPlanet)
        Gui.instance.updateMutationToggle()
        Gui.instance.updateSimulationTimeDisplay()
        Gui.instance.resetMapPreviewCenter()
        planetRenderer.update(newPlanet)
        Gui.instance.statsGraph.planet = newPlanet
        Gui.instance.brushTool.refreshOptions()
        Gui.instance.updateHistoryDisplay()
        Gui.instance.treeOfLifeView.refresh()
    }

    @RegisterFunction
    override fun _exitTree() {
        simulationRunner.close()
    }

    companion object {
        lateinit var instance: Main
        val debugRandom = Random(0)

        val simulations = mapOf(
            "tectonics" to { planet: Planet -> Tectonics.stepTectonicsSimulation(planet) },
            "climate" to { planet: Planet -> ClimateSimulation.stepClimateSimulation(planet) },
            "erosion" to { planet: Planet -> Erosion.performErosion(planet) }
        )
    }
}
