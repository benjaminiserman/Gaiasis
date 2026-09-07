package dev.biserman.planet.gui

import dev.biserman.planet.Main
import dev.biserman.planet.planet.ecology.EarthTreeOfLife
import dev.biserman.planet.planet.ecology.EcoStrategy
import dev.biserman.planet.planet.ecology.Habitat
import dev.biserman.planet.planet.ecology.PlanetEcology
import dev.biserman.planet.planet.ecology.ProducerCompetitionLayer
import dev.biserman.planet.planet.ecology.SpeciesDefinition
import dev.biserman.planet.planet.ecology.SpeciesTrait
import dev.biserman.planet.planet.ecology.TraitDifferenceKind
import dev.biserman.planet.planet.ecology.TreeOfLifeInclusion
import godot.api.Button
import godot.api.CheckButton
import godot.api.Control
import godot.api.Label
import godot.api.LineEdit
import godot.api.TextServer
import godot.api.Tree
import godot.api.TreeItem
import godot.core.Color
import godot.core.connect
import java.util.Locale

/** Interactive tree and inspector for the playable species catalog. */
class TreeOfLifeView(private val gui: Gui) {
    private val showButton by lazy { gui.findChild("TreeOfLifeButton") as Button }
    private val panel by lazy { gui.findChild("TreeOfLifeView") as Control }
    private val closeButton by lazy { gui.findChild("CloseTreeOfLifeButton") as Button }
    private val search by lazy { gui.findChild("TreeOfLifeSearch") as LineEdit }
    private val searchStatus by lazy { gui.findChild("TreeOfLifeSearchStatus") as Label }
    private val tree by lazy { gui.findChild("TreeOfLifeTree") as Tree }
    private val detailsTitle by lazy { gui.findChild("TreeOfLifeDetailsTitle") as Label }
    private val details by lazy { gui.findChild("TreeOfLifeDetails") as Label }
    private val compiledTitle by lazy { gui.findChild("TreeOfLifeCompiledTitle") as Label }
    private val compiledDetails by lazy { gui.findChild("TreeOfLifeCompiledDetails") as Tree }
    private val traitsTitle by lazy { gui.findChild("TreeOfLifeTraitsTitle") as Label }
    private val traitDifferencesOnly by lazy { gui.findChild("TreeOfLifeTraitDifferencesOnly") as CheckButton }
    private val traitsTree by lazy { gui.findChild("TreeOfLifeTraits") as Tree }

    private val definitionsByItem = mutableMapOf<TreeItem, SpeciesDefinition>()
    private val itemsById = mutableMapOf<String, TreeItem>()
    private val traitDescriptionsByItem = mutableMapOf<TreeItem, TreeItem>()
    private val traitNamesByItem = mutableMapOf<TreeItem, String>()
    private var selectedNodeId: String? = null
    private var expandedTrait: TreeItem? = null
    private var updatingTree = false
    private var builtTreeRevision = -1L
    private var currentlyExtantSpeciesIds = emptySet<String>()

    fun initialize() {
        tree.setColumns(3)
        tree.setColumnTitlesVisible(true)
        tree.setColumnTitle(0, "Randomize?")
        tree.setColumnTitle(1, "Lineage")
        tree.setColumnTitle(2, "Status")
        tree.setColumnCustomMinimumWidth(0, 100)
        tree.setColumnCustomMinimumWidth(1, 290)
        tree.setColumnCustomMinimumWidth(2, 150)
        tree.setColumnExpand(0, false)
        tree.setColumnExpand(1, true)
        tree.setColumnExpand(2, false)
        tree.setHideRoot(true)
        traitsTree.setColumns(1)
        traitsTree.setHideRoot(true)
        traitsTree.setAllowReselect(true)
        compiledDetails.setColumns(2)
        compiledDetails.setColumnExpand(0, true)
        compiledDetails.setColumnExpand(1, false)
        compiledDetails.setColumnCustomMinimumWidth(1, 125)
        compiledDetails.setHideRoot(true)

        showButton.pressed.connect {
            panel.visible = true
            refresh()
        }
        closeButton.pressed.connect { close() }
        search.textChanged.connect { value -> applySearch(value) }
        tree.itemSelected.connect {
            val item = tree.getSelected() ?: return@connect
            val definition = definitionsByItem[item] ?: return@connect
            selectedNodeId = definition.id
            updateDetails(definition)
        }
        tree.itemEdited.connect {
            if (updatingTree || !Main.instance.hasPlanet || tree.getEditedColumn() != 0) return@connect
            val item = tree.getEdited() ?: return@connect
            val definition = definitionsByItem[item] ?: return@connect
            EarthTreeOfLife.setIncluded(
                node = definition,
                included = item.isChecked(0),
                excludedSpeciesIds = Main.instance.planet.randomEcosystemSpeciesIdsExcluded,
            )
            updateInclusionChecks()
            selectedNodeId?.let(EarthTreeOfLife.nodesById::get)?.let(::updateDetails)
        }
        traitDifferencesOnly.toggled.connect {
            selectedNodeId?.let(EarthTreeOfLife.nodesById::get)?.let(::updateDetails)
        }
        traitsTree.itemSelected.connect {
            val item = traitsTree.getSelected() ?: return@connect
            val description = traitDescriptionsByItem[item] ?: return@connect
            val wasExpanded = expandedTrait == item
            expandedTrait?.let { collapseTrait(it) }
            if (wasExpanded) {
                expandedTrait = null
            } else {
                description.setVisible(true)
                item.setText(0, "▼ ${traitNamesByItem.getValue(item)}")
                expandedTrait = item
            }
        }
    }

    fun setPlayModeEnabled(enabled: Boolean) {
        showButton.visible = enabled
        if (!enabled) close()
    }

    fun refresh() {
        if (!panel.visible || !Main.instance.hasPlanet) return

        currentlyExtantSpeciesIds = EarthTreeOfLife.currentlyExtantSpeciesIds(Main.instance.planet)
        if (itemsById.isEmpty() || builtTreeRevision != EarthTreeOfLife.revision) buildTree()
        updateInclusionChecks()
        updateLineageStatuses()
        applySearch(search.text)

        val selection = selectedNodeId?.let(EarthTreeOfLife.nodesById::get)
            ?: EarthTreeOfLife.roots.firstOrNull()
        if (selection == null) {
            detailsTitle.text = "No species"
            details.text = "The extant species catalog is empty."
            return
        }
        selectedNodeId = selection.id
        itemsById[selection.id]?.select(1)
        updateDetails(selection)
    }

    private fun buildTree() {
        definitionsByItem.clear()
        itemsById.clear()
        tree.clear()
        val root = tree.createItem() ?: return
        EarthTreeOfLife.roots.sortedBy { it.displayName }.forEach { addNode(it, root) }
        builtTreeRevision = EarthTreeOfLife.revision
    }

    private fun addNode(
        definition: SpeciesDefinition,
        parent: TreeItem,
    ) {
        val item = tree.createItem(parent) ?: return
        definitionsByItem[item] = definition
        itemsById[definition.id] = item
        item.setCellMode(0, TreeItem.TreeCellMode.CELL_MODE_CHECK)
        item.setEditable(0, true)
        item.setSelectable(0, false)
        item.setTooltipText(0, "Toggle this node and all extant descendants for Randomize Ecosystems")
        item.setText(1, definition.displayName)
        item.setTooltipText(1, "Inspect ${definition.displayName}")

        item.setSelectable(1, true)
        item.setSelectable(2, true)
        EarthTreeOfLife.visibleChildren(definition).forEach { addNode(it, item) }
    }

    private fun updateLineageStatuses() {
        definitionsByItem.forEach { (item, definition) ->
            val descendantCount = EarthTreeOfLife.extantDescendants(definition).count {
                it.id != definition.id && it.id in currentlyExtantSpeciesIds
            }
            val isCatalogSpecies = EarthTreeOfLife.isExtant(definition)
            val isCurrentlyExtant = definition.id in currentlyExtantSpeciesIds
            val extinctNode = if (isCatalogSpecies) !isCurrentlyExtant else descendantCount == 0
            if (extinctNode) {
                item.setCustomColor(1, EXTINCT_NAME_COLOR)
            } else {
                item.clearCustomColor(1)
            }
            item.setText(
                2,
                when {
                    isCurrentlyExtant -> buildString {
                        append("extant species")
                        if (descendantCount > 0) {
                            append(" · ")
                            append(descendantCount)
                            append(" extant descendant")
                            if (descendantCount != 1) append('s')
                        }
                    }
                    isCatalogSpecies -> buildString {
                        append("extinct species")
                        if (descendantCount > 0) {
                            append(" · ")
                            append(descendantCount)
                            append(" extant descendant")
                            if (descendantCount != 1) append('s')
                        }
                    }
                    else -> "$descendantCount extant descendant${if (descendantCount == 1) "" else "s"}"
                },
            )
        }
    }

    private fun updateInclusionChecks() {
        val excluded = Main.instance.planet.randomEcosystemSpeciesIdsExcluded
        updatingTree = true
        definitionsByItem.forEach { (item, definition) ->
            val inclusion = EarthTreeOfLife.inclusion(definition, excluded)
            item.setChecked(0, inclusion != TreeOfLifeInclusion.EXCLUDED)
            item.setIndeterminate(0, inclusion == TreeOfLifeInclusion.MIXED)
        }
        updatingTree = false
    }

    /** Name matches take precedence; trait names and descriptions are the fallback. */
    private fun applySearch(value: String) {
        if (itemsById.isEmpty()) return
        val query = value.trim().lowercase(Locale.ROOT)
        if (query.isEmpty()) {
            EarthTreeOfLife.roots.forEach { applySearchVisibility(it, emptySet(), false) }
            searchStatus.text = "${currentlyExtantSpeciesIds.size} extant species"
            return
        }

        val nodes = EarthTreeOfLife.nodesById.values
        val nameMatches = nodes.filterTo(mutableSetOf()) { node ->
            EarthTreeOfLife.inspectionDefinition(node).displayName.lowercase(Locale.ROOT).contains(query)
        }
        val matches = nameMatches.ifEmpty {
            nodes.filterTo(mutableSetOf()) { node ->
                EarthTreeOfLife.inspectionDefinition(node).traits.any { trait ->
                    trait.displayName.lowercase(Locale.ROOT).contains(query) ||
                        trait.description.lowercase(Locale.ROOT).contains(query)
                }
            }
        }
        EarthTreeOfLife.roots.forEach { applySearchVisibility(it, matches, true) }
        searchStatus.text = when {
            matches.isEmpty() -> "No matches"
            nameMatches.isNotEmpty() -> "${matches.size} name match${if (matches.size == 1) "" else "es"}"
            else -> "${matches.size} trait match${if (matches.size == 1) "" else "es"}"
        }
    }

    private fun applySearchVisibility(
        definition: SpeciesDefinition,
        matches: Set<SpeciesDefinition>,
        filtering: Boolean,
    ): Boolean {
        var childVisible = false
        EarthTreeOfLife.visibleChildren(definition).forEach { child ->
            childVisible = applySearchVisibility(child, matches, filtering) || childVisible
        }
        val visible = !filtering || definition in matches || childVisible
        itemsById[definition.id]?.let { item ->
            item.setVisible(visible)
            if (filtering && childVisible) item.setCollapsed(false)
        }
        return visible
    }

    private fun updateDetails(definition: SpeciesDefinition) {
        val planet = Main.instance.planet
        val inspected = EarthTreeOfLife.inspectionDefinition(definition)
        val descendants = EarthTreeOfLife.extantDescendants(definition)
        val descendantCount = descendants.count {
            it.id != definition.id && it.id in currentlyExtantSpeciesIds
        }
        val isCatalogSpecies = EarthTreeOfLife.isExtant(definition)
        val isCurrentlyExtant = definition.id in currentlyExtantSpeciesIds
        val population = if (isCatalogSpecies) {
            EarthTreeOfLife.ownGlobalPopulation(planet, definition)
        } else {
            EarthTreeOfLife.globalPopulation(planet, definition)
        }
        val descendantPopulation = if (isCatalogSpecies && descendantCount > 0) {
            EarthTreeOfLife.descendantGlobalPopulation(planet, definition)
        } else {
            null
        }
        val inclusion = EarthTreeOfLife.inclusion(
            definition,
            planet.randomEcosystemSpeciesIdsExcluded,
        )
        val lineageStatus = when {
            isCurrentlyExtant -> buildString {
                append("Extant species")
                if (descendantCount > 0) {
                    append(" with ")
                    append(descendantCount)
                    append(" extant descendant species")
                }
            }
            isCatalogSpecies -> buildString {
                append("Extinct species")
                if (descendantCount > 0) {
                    append(" with ")
                    append(descendantCount)
                    append(" extant descendant species")
                }
            }
            else -> "Ancestral lineage containing $descendantCount extant species"
        }
        val randomizationStatus = when (inclusion) {
            TreeOfLifeInclusion.INCLUDED -> "Included"
            TreeOfLifeInclusion.EXCLUDED -> "Excluded"
            TreeOfLifeInclusion.MIXED -> "Mixed (some descendants excluded)"
        }
        detailsTitle.text = inspected.displayName.replaceFirstChar { it.titlecase() }
        details.text = buildString {
            appendLine(lineageStatus)
            appendLine("Size class: ${inspected.sizeClass.name.lowercase().replace('_', ' ')}")
            appendLine("Randomize Ecosystems: $randomizationStatus")
            appendLine()
            appendPopulation(
                title = if (isCatalogSpecies) {
                    "Current global population (this species)"
                } else {
                    "Current clade population"
                },
                population = population,
            )
            if (!isCatalogSpecies) {
                appendLine("Population totals aggregate all extant descendants.")
            }
            descendantPopulation?.let {
                appendLine()
                appendPopulation(
                    title = "Descendant clade population ($descendantCount species)",
                    population = it,
                )
            }
        }
        buildCompiledDetails(inspected)
        val ancestor = EarthTreeOfLife.directAncestor(definition)
        traitDifferencesOnly.disabled = ancestor == null
        if (traitDifferencesOnly.buttonPressed && ancestor != null) {
            val differences = EarthTreeOfLife.traitDifferencesFromAncestor(definition)
            traitsTitle.text = "Trait differences (${differences.size})"
            buildTraits(
                differences.map { difference ->
                    val change = when (difference.kind) {
                        TraitDifferenceKind.ADDED -> "Added"
                        TraitDifferenceKind.REMOVED -> "Removed"
                    }
                    DisplayedTrait(
                        trait = difference.trait,
                        name = "${if (difference.kind == TraitDifferenceKind.ADDED) "+" else "−"} " +
                            difference.trait.displayName,
                        description = "$change relative to ${ancestor.displayName}.\n\n" +
                            difference.trait.description,
                    )
                },
            )
        } else {
            traitsTitle.text = "Traits (${inspected.traits.size})"
            buildTraits(inspected.traits.map(::DisplayedTrait))
        }
    }

    private fun buildCompiledDetails(definition: SpeciesDefinition) {
        compiledDetails.clear()
        val compiled = PlanetEcology.compiled.species.firstOrNull { it.id == definition.id }
        if (compiled == null) {
            compiledTitle.text = "Compiled ecology unavailable for ancestral lineages"
            compiledDetails.visible = false
            return
        }
        compiledTitle.text = "Compiled ecology"
        compiledDetails.visible = true
        val root = compiledDetails.createItem() ?: return

        val habitats = compiledDetails.createItem(root) ?: return
        habitats.setText(0, "Habitats")
        habitats.setText(1, "access · affinity")
        habitats.setSelectable(0, false)
        habitats.setSelectable(1, false)
        Habitat.entries.forEach { habitat ->
            val item = compiledDetails.createItem(habitats) ?: return@forEach
            item.setText(0, habitat.displayName.toDisplayText())
            item.setText(
                1,
                if (compiled.niche.accesses(habitat)) {
                    "yes · ${formatPercent(compiled.niche.supportFor(habitat))}"
                } else {
                    "no · —"
                },
            )
            item.setSelectable(0, false)
            item.setSelectable(1, false)
        }

        val strategies = compiledDetails.createItem(root) ?: return
        strategies.setText(0, "Ecological strategies")
        strategies.setText(1, "access · affinity")
        strategies.setSelectable(0, false)
        strategies.setSelectable(1, false)
        EcoStrategy.entries.forEach { strategy ->
            val item = compiledDetails.createItem(strategies) ?: return@forEach
            item.setText(0, strategy.displayName.toDisplayText())
            item.setText(
                1,
                if (compiled.niche.accesses(strategy)) {
                    "yes · ${formatPercent(compiled.niche.supportFor(strategy))}"
                } else {
                    "no · —"
                },
            )
            item.setSelectable(0, false)
            item.setSelectable(1, false)
        }

        val profile = compiledDetails.createItem(root) ?: return
        profile.setText(0, "Headline profile")
        profile.setText(1, "compiled value")
        profile.setSelectable(0, false)
        profile.setSelectable(1, false)
        val thermal = compiled.physiology.thermal
        val hydration = compiled.physiology.hydration
        val respiration = compiled.physiology.respiration
        val lifeHistory = compiled.lifeHistory
        addCompiledDetail(profile, "Typical body mass", "${formatAmount(compiled.physiology.massKg)} kg")
        addCompiledDetail(profile, "Maintenance demand", formatAmount(compiled.physiology.maintenanceDemand))
        addCompiledDetail(profile, "Thermal regulation", thermal.regulation?.name.toDisplayText())
        addCompiledDetail(profile, "Optimal temperature", "${formatNumber(thermal.optimalLowC)}–${formatNumber(thermal.optimalHighC)} °C")
        addCompiledDetail(profile, "Survival temperature", "${formatNumber(thermal.outerLowC)}–${formatNumber(thermal.outerHighC)} °C")
        addCompiledDetail(
            profile,
            "Water tolerance",
            "${formatPercent(hydration.minimumWater)} min · ${formatPercent(hydration.maximumWater)} max",
        )
        addCompiledDetail(
            profile,
            "Respiration",
            buildList {
                if (respiration.aerialBreathing) add("air")
                if (respiration.underwaterBreathing) add("water")
                if (respiration.prolongedBreathHolding) add("breath-holding")
            }.joinToString().ifEmpty { "none" },
        )
        addCompiledDetail(profile, "Salinity", respiration.salinityTolerance.name.toDisplayText())
        addCompiledDetail(profile, "Seasonal reproduction", formatNumber(lifeHistory.seasonalReproduction))
        addCompiledDetail(profile, "Mutation rate", "×${formatNumber(lifeHistory.mutationRateMultiplier)}")
        addCompiledDetail(profile, "Energy reserves", formatPercent(lifeHistory.reserveCapacity))
        addCompiledDetail(profile, "Dormancy", lifeHistory.dormancyKind.name.toDisplayText())
        addCompiledDetail(profile, "Dispersal", lifeHistory.dispersalKind.name.toDisplayText())
        addCompiledDetail(profile, "Capture / defense", "${formatNumber(compiled.interactions.captureAbility)} / ${formatNumber(compiled.interactions.defense)}")
        addCompiledDetail(profile, "Pursuit / sensing", "${formatPercent(compiled.interactions.pursuitSpeed)} / ${formatPercent(compiled.interactions.sensing)}")
        if (compiled.niche.producerCompetitionLayer != ProducerCompetitionLayer.NONE) {
            addCompiledDetail(
                profile,
                "Producer layer",
                compiled.niche.producerCompetitionLayer.name.toDisplayText(),
            )
        }
    }

    private fun addCompiledDetail(
        parent: TreeItem,
        name: String,
        value: String,
    ) {
        val item = compiledDetails.createItem(parent) ?: return
        item.setText(0, name)
        item.setText(1, value)
        item.setSelectable(0, false)
        item.setSelectable(1, false)
    }

    private fun buildTraits(traits: List<DisplayedTrait>) {
        expandedTrait = null
        traitDescriptionsByItem.clear()
        traitNamesByItem.clear()
        traitsTree.clear()
        val root = traitsTree.createItem() ?: return
        traits.forEach { displayed ->
            val item = traitsTree.createItem(root) ?: return@forEach
            traitNamesByItem[item] = displayed.name
            item.setText(0, "▶ ${displayed.name}")
            item.setTooltipText(0, "Click to show or hide this trait's description")
            item.setDisableFolding(true)
            val description = traitsTree.createItem(item) ?: return@forEach
            traitDescriptionsByItem[item] = description
            description.setText(0, displayed.description)
            description.setAutowrapMode(0, TextServer.AutowrapMode.WORD_SMART)
            description.setSelectable(0, false)
            description.setVisible(false)
        }
    }

    private fun collapseTrait(item: TreeItem) {
        traitDescriptionsByItem[item]?.setVisible(false)
        traitNamesByItem[item]?.let { item.setText(0, "▶ $it") }
    }

    private fun StringBuilder.appendPopulation(
        title: String,
        population: dev.biserman.planet.planet.ecology.GlobalPopulation,
    ) {
        appendLine(title)
        appendLine("Individuals: ${formatAmount(population.individuals)}")
        appendLine("Biomass: ${formatAmount(population.biomassKg)} kg")
        appendLine("Occupied tiles: ${population.occupiedTiles}")
    }

    private fun close() {
        panel.visible = false
    }

    private fun formatAmount(value: Double): String = when {
        value == 0.0 -> "0"
        value >= 10_000.0 || value < 0.01 -> String.format(Locale.ROOT, "%.2e", value)
        else -> String.format(Locale.ROOT, "%,.2f", value)
    }

    private fun formatNumber(value: Double): String = String.format(Locale.ROOT, "%.2f", value)

    private fun formatPercent(value: Double): String = String.format(Locale.ROOT, "%.0f%%", value * 100.0)

    private fun String?.toDisplayText(): String =
        this?.lowercase(Locale.ROOT)?.replace('_', ' ')?.replace('-', ' ') ?: "none"

    private data class DisplayedTrait(
        val trait: SpeciesTrait,
        val name: String = trait.displayName,
        val description: String = trait.description,
    )

    companion object {
        private val EXTINCT_NAME_COLOR = Color(0.5, 0.5, 0.5, 1.0)
    }
}
