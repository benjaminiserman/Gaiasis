package dev.biserman.planet.rendering.colormodes

import dev.biserman.planet.planet.PlanetTile
import dev.biserman.planet.rendering.PlanetColorMode
import dev.biserman.planet.rendering.PlanetRenderer
import godot.core.Color

class SimpleColorMode(
    planetRenderer: PlanetRenderer,
    override val name: String,
    override val categories: List<String>,
    val getFn: (PlanetTile) -> Color?,
) : PlanetColorMode(planetRenderer) {
    override fun colorFor(planetTile: PlanetTile): Color? = getFn(planetTile)
}
