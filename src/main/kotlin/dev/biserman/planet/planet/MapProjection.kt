package dev.biserman.planet.planet

import dev.biserman.planet.geometry.GeoPoint
import dev.biserman.planet.geometry.Kriging
import dev.biserman.planet.geometry.toGeoPoint
import dev.biserman.planet.geometry.toPoint
import dev.biserman.planet.geometry.toVector2
import dev.biserman.planet.geometry.toVector3
import godot.core.Color
import godot.core.Vector2
import godot.core.Vector3
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.roundToInt

class MapProjection(val forward: (GeoPoint) -> Vector2, val backward: (Vector2) -> GeoPoint)

object MapProjections {
    val EQUIRECTANGULAR = MapProjection({ geoPoint -> geoPoint.toVector2() }, { vector2 -> GeoPoint(vector2) })
    val EQUIDISTANT = EQUIRECTANGULAR

    fun (MapProjection).projectPoints(
        planet: Planet,
        imageName: String?,
        imageX: Int,
        imageY: Int,
        dateLine: Double = planet.internationalDateLine,
        colorFn: (Planet).(Vector3) -> Color
    ): BufferedImage {
        val image = BufferedImage(imageX, imageY, BufferedImage.TYPE_INT_ARGB)
//        val edgeX = forward(planet.pointNemo.tile.position.toGeoPoint()).x
        val edgeX = forward(
            GeoPoint(0.0, dateLine)
        ).x

        for (x in 0..<imageX) {
            for (y in 0..<imageY) {
                val startX = 1 - ((x.toDouble() / imageX) - 0.5)
                val offset = edgeX + 0.5
                val newX = if (startX + offset > 1) startX + offset - 1 else startX + offset
                image.setRGB(
                    x,
                    y,
                    planet.colorFn(
                        this.backward(
                            Vector2(
                                newX,
                                -(y.toDouble() / imageY - 0.5),
                            )
                        ).toVector3()
                    ).clamp(Color.black, Color.white).toARGB32()
                )
            }
        }

        if (imageName != null) {
            ImageIO.write(image, "png", File(imageName))
        }
        return image
    }

    fun (MapProjection).projectTiles(
        planet: Planet,
        imageName: String?,
        imageX: Int,
        imageY: Int,
        useKriging: Boolean = true,
        sampleRadius: Double = planet.topology.averageRadius,
        variogram: (Double) -> Double = Kriging.variogram(sampleRadius, 1.0, 0.0),
        dateLine: Double = planet.internationalDateLine,
        colorFn: (PlanetTile) -> Color,
    ): BufferedImage =
        this.projectPoints(
            planet,
            imageName,
            imageX,
            imageY,
            dateLine
        ) { point ->
            if (!useKriging) {
                val nearestTile = this.topology.rTree
                    .nearest(point.toPoint(), sampleRadius, 1)
                    .first()
                    .value()
                return@projectPoints colorFn(planet.getTile(nearestTile))
            }

            val nearest = this.topology.rTree.nearest(point.toPoint(), sampleRadius, 10)
                .map { it.value().position to colorFn(planet.getTile(it.value())) }
            val nearestR = nearest.map { (position, color) -> position to color.r }
            val nearestG = nearest.map { (position, color) -> position to color.g }
            val nearestB = nearest.map { (position, color) -> position to color.b }
            val nearestA = nearest.map { (position, color) -> position to color.a }

            Color(
                Kriging.interpolate(nearestR, point, variogram),
                Kriging.interpolate(nearestG, point, variogram),
                Kriging.interpolate(nearestB, point, variogram),
                Kriging.interpolate(nearestA, point, variogram),
            )
        }

    fun (MapProjection).projectTileIds(
        planet: Planet,
        imageX: Int,
        imageY: Int,
        sampleRadius: Double = planet.topology.averageRadius,
        dateLine: Double = planet.internationalDateLine,
    ): IntArray {
        val result = IntArray(imageX * imageY)
        val edgeX = forward(GeoPoint(0.0, dateLine)).x

        for (x in 0..<imageX) {
            for (y in 0..<imageY) {
                val startX = 1 - (x.toDouble() / imageX - 0.5)
                val offset = edgeX + 0.5
                val newX = if (startX + offset > 1) startX + offset - 1 else startX + offset
                val point = backward(Vector2(newX, -(y.toDouble() / imageY - 0.5))).toVector3()
                result[y * imageX + x] = planet.topology.rTree
                    .nearest(point.toPoint(), sampleRadius, 1)
                    .first()
                    .value()
                    .id
            }
        }
        return result
    }

    fun (MapProjection).applyValueTo(
        planet: Planet,
        imageName: String,
        progress: ((fraction: Double, status: String) -> Unit)? = null,
        modifyFn: (PlanetTile).(Color) -> Unit,
    ) {
        progress?.invoke(0.02, "Reading elevation image")
        val image = ImageIO.read(File(imageName))
        progress?.invoke(0.08, "Applying elevation to tiles")
        val tiles = planet.planetTiles.values
        tiles.forEachIndexed { index, tile ->
            val projected = forward(tile.tile.position.toGeoPoint())
            val pixelX = ((0.5 - projected.x) * image.width).roundToInt().coerceIn(0, image.width - 1)
            val pixelY = ((0.5 - projected.y) * image.height - 1).roundToInt().coerceIn(0, image.height - 1)
            modifyFn(tile, image.getRGB(pixelX, pixelY).toRGB())
            if (index % IMPORT_PROGRESS_INTERVAL == 0 || index == tiles.size - 1) {
                progress?.invoke(
                    0.08 + 0.82 * (index + 1).toDouble() / tiles.size,
                    "Applying elevation to tiles",
                )
            }
        }
        progress?.invoke(0.9, "Elevation applied")
    }

    // turn java RGB int to Godot color
    private fun Int.toRGB(): Color {
        val r = (this shr 16) and 0xFF
        val g = (this shr 8) and 0xFF
        val b = this and 0xFF
        return Color(r / 255.0, g / 255.0, b / 255.0)
    }

    private const val IMPORT_PROGRESS_INTERVAL = 64
}
