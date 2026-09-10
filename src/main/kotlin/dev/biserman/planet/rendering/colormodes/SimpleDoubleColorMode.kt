package dev.biserman.planet.rendering.colormodes

import dev.biserman.planet.planet.PlanetTile
import dev.biserman.planet.rendering.PlanetColorMode
import dev.biserman.planet.rendering.PlanetRenderer
import godot.core.Color

class SimpleDoubleColorMode(
    planetRenderer: PlanetRenderer,
    override val name: String,
    val colorFn: (Double?) -> Color = defaultColorFn,
    override val categories: List<String>,
    val getFn: (PlanetTile) -> Double?,
) : PlanetColorMode(planetRenderer) {
    override fun colorFor(planetTile: PlanetTile): Color = colorFn(getFn(planetTile))

    companion object {
        val defaultColorFn = redWhenNull { Color(it, it, it, 1.0) }

        fun redOutsideRange(range: ClosedRange<Double>, colorFn: (Double) -> Color = defaultColorFn) =
            redWhenNull { if (it in range) colorFn(it) else Color.red }

        fun redWhenNull(colorFn: (Double) -> Color) = { level: Double? ->
            if (level == null) Color.red else colorFn(level)
        }
    }
}
