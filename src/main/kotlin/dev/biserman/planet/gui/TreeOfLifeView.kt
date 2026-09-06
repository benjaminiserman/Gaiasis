package dev.biserman.planet.gui

import dev.biserman.planet.Main
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.EarthTreeOfLife
import dev.biserman.planet.planet.ecology.SpeciesDefinition
import dev.biserman.planet.planet.ecology.TreeOfLifeInclusion
import godot.api.Button
import godot.api.Control
import godot.api.Label
import godot.api.LineEdit
import godot.api.TextServer
import godot.api.Tree
import godot.api.TreeItem
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
    private val traitsTitle by lazy { gui.findChild("TreeOfLifeTraitsTitle") as Label }
    private val traitsTree by lazy { gui.findChild("TreeOfLifeTraits") as Tree }

    private val definitionsByItem = mutableMapOf<TreeItem, SpeciesDefinition>()
    private val itemsById = mutableMapOf<String, TreeItem>()
    private val traitDescriptionsByItem = mutableMapOf<TreeItem, TreeItem>()
    private val traitNamesByItem = mutableMapOf<TreeItem, String>()
    private var selectedNodeId: String? = null
    private var expandedTrait: TreeItem? = null
    private var updatingTree = false

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

        if (itemsById.isEmpty()) buildTree()
        updateInclusionChecks()
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

        val descendants = EarthTreeOfLife.extantDescendants(definition)
        item.setText(
            2,
            if (EarthTreeOfLife.isExtant(definition)) {
                "extant species"
            } else {
                "${descendants.size} extant descendant${if (descendants.size == 1) "" else "s"}"
            },
        )
        item.setSelectable(1, true)
        item.setSelectable(2, true)
        EarthTreeOfLife.visibleChildren(definition).forEach { addNode(it, item) }
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
            searchStatus.text = "${EarthSpeciesCatalog.ALL.size} extant species"
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
        val population = EarthTreeOfLife.globalPopulation(planet, definition)
        val inclusion = EarthTreeOfLife.inclusion(
            definition,
            planet.randomEcosystemSpeciesIdsExcluded,
        )
        val lineageStatus = if (EarthTreeOfLife.isExtant(definition)) {
            "Extant species"
        } else {
            "Ancestral lineage containing ${descendants.size} extant species"
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
            appendLine("Current global population")
            appendLine("Individuals: ${formatAmount(population.individuals)}")
            appendLine("Biomass: ${formatAmount(population.biomassKg)} kg")
            appendLine("Occupied tiles: ${population.occupiedTiles}")
            if (!EarthTreeOfLife.isExtant(definition)) {
                appendLine("Population totals aggregate all extant descendants.")
            }
        }
        traitsTitle.text = "Traits (${inspected.traits.size})"
        buildTraits(inspected)
    }

    private fun buildTraits(definition: SpeciesDefinition) {
        expandedTrait = null
        traitDescriptionsByItem.clear()
        traitNamesByItem.clear()
        traitsTree.clear()
        val root = traitsTree.createItem() ?: return
        definition.traits.forEach { trait ->
            val item = traitsTree.createItem(root) ?: return@forEach
            traitNamesByItem[item] = trait.displayName
            item.setText(0, "▶ ${trait.displayName}")
            item.setTooltipText(0, "Click to show or hide this trait's description")
            item.setDisableFolding(true)
            val description = traitsTree.createItem(item) ?: return@forEach
            traitDescriptionsByItem[item] = description
            description.setText(0, trait.description)
            description.setAutowrapMode(0, TextServer.AutowrapMode.WORD_SMART)
            description.setSelectable(0, false)
            description.setVisible(false)
        }
    }

    private fun collapseTrait(item: TreeItem) {
        traitDescriptionsByItem[item]?.setVisible(false)
        traitNamesByItem[item]?.let { item.setText(0, "▶ $it") }
    }

    private fun close() {
        panel.visible = false
    }

    private fun formatAmount(value: Double): String = when {
        value == 0.0 -> "0"
        value >= 10_000.0 || value < 0.01 -> String.format(Locale.ROOT, "%.2e", value)
        else -> String.format(Locale.ROOT, "%,.2f", value)
    }
}
