package dev.biserman.planet.gui

import dev.biserman.planet.planet.Planet
import dev.biserman.planet.planet.PlanetStats
import dev.biserman.planet.planet.Stat
import dev.biserman.planet.utils.component1
import dev.biserman.planet.utils.component2
import godot.api.CanvasItem
import godot.api.CheckButton
import godot.api.Control
import godot.api.Label
import godot.api.MenuButton
import godot.api.VBoxContainer
import godot.core.Color
import godot.core.Vector2
import godot.core.connect
import godot.global.GD
import kotlin.math.max
import kotlin.time.measureTime

class StatsGraph(val rootNode: CanvasItem) {
    val graph = rootNode.findChild("Graph2d") as StatsGraphPlot
    val menuButton = rootNode.findChild("GraphOptions") as MenuButton
    val currentValueLabel = rootNode.findChild("GraphCurrentValue") as Label
    private val expandedOverlay = rootNode.getParent()!!.findChild("ExpandedStatsGraph") as Control
    private val expandedGraph = expandedOverlay.findChild("ExpandedGraph2d") as StatsGraphPlot
    private val expandedSeriesList = expandedOverlay.findChild("SeriesList") as VBoxContainer
    private val expandedInspectionLabel = expandedOverlay.findChild("InspectionLabel") as Label
    private val expandedCloseButton = expandedOverlay.findChild("CloseButton") as godot.api.Button
    private val expandedResetButton = expandedOverlay.findChild("ResetViewButton") as godot.api.Button
    private val expandedSelections = linkedSetOf<String>()
    private val expandedSeriesButtons = mutableListOf<CheckButton>()
    private val expandedIntegerValues = mutableMapOf<String, Boolean>()

    private lateinit var stats: PlanetStats
    private var historyMode = false
    private val activeStats get() = if (historyMode) stats.historyStats else stats.tectonicStats
    var planet: Planet? = null
        set(value) {
            field = value
            if (value != null) {
                stats = value.planetStats
                menuButton.getPopup()!!.clear()
                activeStats.forEach { stat ->
                    menuButton.getPopup()!!.addItem(stat.name)
                }
                shownStat = null
                closeExpanded()
            }
        }

    private val statValues
        get() = if (historyMode) planet!!.planetStats.historyStatValues else planet!!.planetStats.tectonicStatValues

    var visible = true
        set(value) {
            rootNode.visible = value
            field = value
        }

    private var insertGapBeforeNextSample = false
    var trackStats = true
        set(value) {
            if (!field && value) insertGapBeforeNextSample = true
            field = value
        }

    init {
        menuButton.getPopup()!!.idPressed.connect { shownStat = activeStats[it.toInt()] }
        graph.onExpandRequested = ::openExpanded
        expandedGraph.interactive = true
        expandedGraph.onInspectionChanged = ::showExpandedInspection
        expandedCloseButton.pressed.connect { closeExpanded() }
        expandedResetButton.pressed.connect { expandedGraph.resetView() }
    }

    fun setHistoryMode(enabled: Boolean) {
        if (historyMode != enabled) closeExpanded()
        historyMode = enabled
        expandedIntegerValues.clear()
        graph.xLabel = if (enabled) "Years" else "Million years"
        if (::stats.isInitialized) rebuildMenu()
    }

    private fun rebuildMenu() {
        menuButton.getPopup()!!.clear()
        activeStats.forEach { menuButton.getPopup()!!.addItem(it.name) }
        shownStat = null
    }

    var shownStat: Stat<*>? = null
        set(value) {
            field = value
            graph.clear()
            if (value != null) {
                graph.yLabel = value.yLabel
                graph.integerYLabels = value.usesIntegerValues(planet!!)
                graph.setPoints(statValues[value.name] ?: emptyList())
                rescale(value)
                updateCurrentValue(value, planet!!)
                menuButton.setText(value.name + " ▽")
            } else {
                graph.yLabel = ""
                menuButton.setText("Select Graph")
                currentValueLabel.text = ""
            }
        }

    fun update(planet: Planet) {
        if (!trackStats) {
            shownStat?.let { updateCurrentValue(it, planet) }
            return
        }

        var currentShownValue: Number? = null
        val timeTaken = measureTime {
            activeStats.forEach { stat ->
                val value = stat.getter(planet)
                val time = if (historyMode) planet.historyTurn / 4.0 else planet.tectonicAge.toDouble()
                val values = statValues[stat.name] ?: return@forEach
                val point = Vector2(time, value.toDouble())
                if (insertGapBeforeNextSample && values.isNotEmpty()) {
                    values.add(Vector2(time, Double.NaN))
                    values.add(point)
                } else if (values.lastOrNull()?.x == time) {
                    values[values.lastIndex] = point
                } else {
                    values.add(point)
                }
                if (stat == shownStat) currentShownValue = value
            }
            insertGapBeforeNextSample = false
        }
        currentShownValue?.let(::setCurrentValue)

        GD.print("Updating stats graph took ${timeTaken.inWholeMilliseconds}ms")

        if (shownStat != null) {
            graph.setPoints(statValues[shownStat!!.name]!!)
            rescale(shownStat!!)
        }
        if (expandedOverlay.visible) refreshExpandedGraph()
    }

    private fun openExpanded() {
        if (planet == null) return
        expandedOverlay.visible = true
        expandedSelections.clear()
        expandedSelections.add(shownStat?.name ?: activeStats.firstOrNull()?.name ?: return)
        rebuildExpandedSeriesButtons()
        refreshExpandedGraph(resetView = true)
    }

    private fun closeExpanded() {
        expandedOverlay.visible = false
        expandedGraph.clear()
        expandedSelections.clear()
        expandedSeriesButtons.forEach { it.queueFree() }
        expandedSeriesButtons.clear()
    }

    private fun rebuildExpandedSeriesButtons() {
        expandedSeriesButtons.forEach { it.queueFree() }
        expandedSeriesButtons.clear()
        activeStats.forEachIndexed { index, stat ->
            val button = CheckButton().apply {
                text = stat.name
                buttonPressed = stat.name in expandedSelections
                focusMode = Control.FocusMode.NONE
                modulate = seriesColor(index)
                toggled.connect { selected ->
                    if (selected) expandedSelections.add(stat.name) else expandedSelections.remove(stat.name)
                    refreshExpandedGraph()
                }
            }
            expandedSeriesList.addChild(button)
            expandedSeriesButtons.add(button)
        }
    }

    private fun refreshExpandedGraph(resetView: Boolean = false) {
        val currentPlanet = planet ?: return
        expandedGraph.xLabel = if (historyMode) "Years" else "Million years"
        val selectedSeries = activeStats.mapIndexedNotNull { index, stat ->
            if (stat.name !in expandedSelections) return@mapIndexedNotNull null
            StatsGraphSeries(
                name = stat.name,
                color = seriesColor(index),
                points = statValues[stat.name] ?: emptyList(),
                integerValues = expandedIntegerValues.getOrPut(stat.name) { stat.usesIntegerValues(currentPlanet) },
                yLabel = stat.yLabel,
            )
        }
        expandedGraph.setSeries(selectedSeries, resetView)
    }

    private fun showExpandedInspection(time: Double?, inspections: List<StatsGraphInspection>) {
        if (time == null || inspections.isEmpty()) {
            expandedInspectionLabel.text = "Move over the graph to inspect values."
            return
        }
        val timeText = if (historyMode) "Year ${formatGraphValue(time)}" else "${formatGraphValue(time)} My"
        val values = inspections.joinToString("   ·   ") { inspection ->
            val value = if (inspection.series.integerValues) {
                inspection.point.y.toLong().toString()
            } else {
                formatGraphValue(inspection.point.y)
            }
            "${inspection.series.name}: $value"
        }
        expandedInspectionLabel.text = "$timeText\n$values"
    }

    private fun seriesColor(index: Int): Color = SERIES_COLORS[index % SERIES_COLORS.size]

    private fun formatGraphValue(value: Double): String = when {
        value == 0.0 -> "0"
        kotlin.math.abs(value) >= 1_000_000.0 || kotlin.math.abs(value) < 0.01 -> String.format("%.3g", value)
        kotlin.math.abs(value) >= 1_000.0 -> String.format("%,.0f", value)
        else -> String.format("%.2f", value)
    }

    private fun updateCurrentValue(stat: Stat<*>, planet: Planet) {
        setCurrentValue(stat.getter(planet))
    }

    private fun setCurrentValue(value: Number) {
        currentValueLabel.text = when (value) {
            is Byte, is Short, is Int, is Long -> value.toLong().toString()
            else -> String.format("%.1f", value.toDouble())
        }
    }

    fun rescale(stat: Stat<*>) {
        val values = statValues[stat.name]!!
        val minX = 0
        val maxX = max(10.0, values.lastOrNull()?.x ?: 10.0)

        val range = stat.range
        val (minY, maxY) = if (range != null) {
            range.start.toDouble() to range.endInclusive.toDouble()
        } else {
            var statMin = Double.POSITIVE_INFINITY
            var statMax = Double.NEGATIVE_INFINITY
            values.forEach { point ->
                if (!point.y.isFinite()) return@forEach
                if (point.y < statMin) statMin = point.y
                if (point.y > statMax) statMax = point.y
            }
            if (values.isEmpty()) {
                statMin = 0.0
                statMax = 0.0
            }
            val padding = max(1.0, (statMax - statMin) * 0.1)
            (statMin - padding) to (statMax + padding)
        }
        graph.setBounds(minX.toDouble(), maxX, minY, maxY)
    }

    companion object {
        private val SERIES_COLORS = listOf(
            Color.html("66c2ff"),
            Color.html("ffb347"),
            Color.html("7ee081"),
            Color.html("ff6b8a"),
            Color.html("c69cff"),
            Color.html("ffe066"),
            Color.html("55d6be"),
            Color.html("ff8c69"),
        )
    }
}
