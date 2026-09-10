package dev.biserman.planet.gui

import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.api.Control
import godot.api.InputEvent
import godot.api.InputEventMouseButton
import godot.api.InputEventMouseMotion
import godot.core.Color
import godot.core.HorizontalAlignment
import godot.core.MouseButton
import godot.core.PackedVector2Array
import godot.core.Rect2
import godot.core.Vector2
import godot.core.connect
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

data class StatsGraphSeries(
    val name: String,
    val color: Color,
    val points: List<Vector2>,
    val integerValues: Boolean = false,
    val yLabel: String = "",
)

data class StatsGraphInspection(val series: StatsGraphSeries, val point: Vector2)

@RegisterClass
class StatsGraphPlot : Control() {
    private var series: List<StatsGraphSeries> = emptyList()
    var interactive = false
    var onExpandRequested: (() -> Unit)? = null
    var onInspectionChanged: ((Double?, List<StatsGraphInspection>) -> Unit)? = null

    var xLabel = "Million years"
        set(value) {
            field = value
            queueRedraw()
        }
    var yLabel = ""
        set(value) {
            field = value
            queueRedraw()
        }
    var integerYLabels = false
        set(value) {
            field = value
            queueRedraw()
        }

    private var xMin = 0.0
    private var xMax = 10.0
    private var yMin = 0.0
    private var yMax = 1.0
    private var fullXMin = 0.0
    private var fullXMax = 10.0
    private var dragging = false
    private var dragDistance = 0.0
    private var hoveredX: Double? = null
    private var pinnedX: Double? = null

    @RegisterFunction
    override fun _ready() {
        resized.connect { queueRedraw() }
        mouseExited.connect {
            if (interactive && pinnedX == null) {
                hoveredX = null
                publishInspection()
                queueRedraw()
            }
        }
    }

    fun clear() {
        series = emptyList()
        queueRedraw()
    }

    fun setPoints(newPoints: List<Vector2>) {
        series = listOf(StatsGraphSeries("", Color.white, newPoints))
        queueRedraw()
    }

    fun setSeries(newSeries: List<StatsGraphSeries>, resetView: Boolean = false) {
        val previousFullMax = fullXMax
        val previousSpan = xMax - xMin
        val followedLatest = xMax >= previousFullMax - previousSpan * FOLLOW_LATEST_TOLERANCE
        series = newSeries
        fullXMin = min(0.0, newSeries.mapNotNull { it.points.firstOrNull()?.x }.minOrNull() ?: 0.0)
        fullXMax = max(10.0, newSeries.mapNotNull { it.points.lastOrNull()?.x }.maxOrNull() ?: 10.0)

        if (resetView || previousSpan <= 0.0 || previousSpan >= previousFullMax - fullXMin) {
            xMin = fullXMin
            xMax = fullXMax
        } else if (followedLatest) {
            xMax = fullXMax
            xMin = (xMax - previousSpan).coerceAtLeast(fullXMin)
        } else {
            setViewRange(xMin, xMax)
        }
        updateVisibleYBounds()
        publishInspection()
        queueRedraw()
    }

    fun resetView() {
        xMin = fullXMin
        xMax = fullXMax
        pinnedX = null
        hoveredX = null
        updateVisibleYBounds()
        publishInspection()
        queueRedraw()
    }

    fun setBounds(xMin: Double, xMax: Double, yMin: Double, yMax: Double) {
        this.xMin = xMin
        this.xMax = xMax.coerceAtLeast(xMin + 1e-9)
        this.yMin = yMin
        this.yMax = yMax.coerceAtLeast(yMin + 1e-9)
        queueRedraw()
    }

    @RegisterFunction
    override fun _guiInput(event: InputEvent?) {
        if (event == null) return
        if (!interactive) {
            if (event is InputEventMouseButton && event.buttonIndex == MouseButton.LEFT && !event.pressed) {
                onExpandRequested?.invoke()
                acceptEvent()
            }
            return
        }

        when (event) {
            is InputEventMouseButton -> handleMouseButton(event)
            is InputEventMouseMotion -> handleMouseMotion(event)
        }
    }

    private fun handleMouseButton(event: InputEventMouseButton) {
        when (event.buttonIndex) {
            MouseButton.WHEEL_UP, MouseButton.WHEEL_DOWN -> {
                if (event.pressed && plotRect().hasPoint(event.position)) {
                    zoomAt(event.position.x, if (event.buttonIndex == MouseButton.WHEEL_UP) ZOOM_IN else ZOOM_OUT)
                    acceptEvent()
                }
            }

            MouseButton.LEFT -> {
                if (event.pressed && plotRect().hasPoint(event.position)) {
                    dragging = true
                    dragDistance = 0.0
                    inspectAt(event.position.x)
                    acceptEvent()
                } else if (!event.pressed && dragging) {
                    dragging = false
                    if (dragDistance < CLICK_DRAG_THRESHOLD) {
                        pinnedX = positionToTime(event.position.x)
                        hoveredX = pinnedX
                        publishInspection()
                        queueRedraw()
                    }
                    acceptEvent()
                }
            }

            MouseButton.RIGHT -> {
                if (event.pressed) {
                    pinnedX = null
                    inspectAt(event.position.x)
                    acceptEvent()
                }
            }

            else -> Unit
        }
    }

    private fun handleMouseMotion(event: InputEventMouseMotion) {
        if (dragging) {
            dragDistance += event.relative.length()
            if (dragDistance >= CLICK_DRAG_THRESHOLD) panByPixels(event.relative.x)
        } else if (pinnedX == null) {
            if (plotRect().hasPoint(event.position)) inspectAt(event.position.x) else clearHover()
        }
        acceptEvent()
    }

    private fun zoomAt(mouseX: Double, factor: Double) {
        val plot = plotRect()
        val fullSpan = fullXMax - fullXMin
        val oldSpan = xMax - xMin
        val newSpan = (oldSpan * factor).coerceIn(max(MINIMUM_X_SPAN, fullSpan / MAX_ZOOM_DIVISOR), fullSpan)
        val anchorFraction = ((mouseX - plot.position.x) / plot.size.x).coerceIn(0.0, 1.0)
        val anchorTime = xMin + oldSpan * anchorFraction
        setViewRange(anchorTime - newSpan * anchorFraction, anchorTime + newSpan * (1.0 - anchorFraction))
        updateVisibleYBounds()
        if (pinnedX == null) inspectAt(mouseX) else publishInspection()
        queueRedraw()
    }

    private fun panByPixels(horizontalPixels: Double) {
        val plotWidth = plotRect().size.x
        if (plotWidth <= 0.0) return
        val timeDelta = -horizontalPixels / plotWidth * (xMax - xMin)
        setViewRange(xMin + timeDelta, xMax + timeDelta)
        updateVisibleYBounds()
        publishInspection()
        queueRedraw()
    }

    private fun setViewRange(requestedMin: Double, requestedMax: Double) {
        val fullSpan = fullXMax - fullXMin
        val requestedSpan = (requestedMax - requestedMin).coerceAtMost(fullSpan)
        xMin = requestedMin.coerceIn(fullXMin, fullXMax - requestedSpan)
        xMax = xMin + requestedSpan
    }

    private fun updateVisibleYBounds() {
        var visibleMin = Double.POSITIVE_INFINITY
        var visibleMax = Double.NEGATIVE_INFINITY
        series.forEach { graphSeries ->
            val fromIndex = graphSeries.points.lowerBound(xMin)
            val toIndex = graphSeries.points.upperBound(xMax)
            for (index in fromIndex..<toIndex) {
                visibleMin = min(visibleMin, graphSeries.points[index].y)
                visibleMax = max(visibleMax, graphSeries.points[index].y)
            }
        }
        if (!visibleMin.isFinite() || !visibleMax.isFinite()) {
            visibleMin = 0.0
            visibleMax = 1.0
        }
        val padding = max(1e-9, (visibleMax - visibleMin) * Y_PADDING)
        yMin = visibleMin - padding
        yMax = visibleMax + padding
        integerYLabels = series.isNotEmpty() && series.all { it.integerValues }
        val labels = series.map { it.yLabel }.filter { it.isNotBlank() }.distinct()
        yLabel = when {
            labels.size == 1 -> labels.single()
            series.size > 1 -> "Shared scale"
            else -> ""
        }
    }

    private fun inspectAt(mouseX: Double) {
        hoveredX = positionToTime(mouseX)
        publishInspection()
        queueRedraw()
    }

    private fun clearHover() {
        if (hoveredX == null) return
        hoveredX = null
        publishInspection()
        queueRedraw()
    }

    private fun positionToTime(mouseX: Double): Double {
        val plot = plotRect()
        val fraction = ((mouseX - plot.position.x) / plot.size.x).coerceIn(0.0, 1.0)
        return xMin + (xMax - xMin) * fraction
    }

    private fun publishInspection() {
        val time = pinnedX ?: hoveredX
        onInspectionChanged?.invoke(time, if (time == null) emptyList() else inspectionsAt(time))
    }

    private fun inspectionsAt(time: Double): List<StatsGraphInspection> = series.mapNotNull { graphSeries ->
        graphSeries.points.nearestTo(time)?.let { StatsGraphInspection(graphSeries, it) }
    }

    private fun plotRect() = Rect2(Vector2(58.0, 24.0), Vector2(size.x - 78.0, size.y - 66.0))

    @RegisterFunction
    override fun _draw() {
        val plot = plotRect()
        if (plot.size.x <= 0.0 || plot.size.y <= 0.0) return

        drawRect(Rect2(Vector2.ZERO, size), Color(0.0, 0.0, 0.0, 0.72))

        val font = getThemeDefaultFont()
        val fontSize = 12
        val gridColor = Color(1.0, 1.0, 1.0, 0.18)
        val axisColor = Color(1.0, 1.0, 1.0, 0.75)
        val xTickCount = if (size.x < 450.0) 5 else 8
        val yTicks = if (integerYLabels) {
            val step = max(1, ceil((yMax - yMin) / xTickCount).toInt())
            generateSequence(floor(yMax).toInt()) { it - step }
                .takeWhile { it >= ceil(yMin).toInt() }
                .map { it.toDouble() }
                .toList()
        } else {
            (0..xTickCount).map { index ->
                yMax - (yMax - yMin) * index.toDouble() / xTickCount
            }
        }

        (0..xTickCount).forEach { index ->
            val fraction = index.toDouble() / xTickCount
            val x = plot.position.x + plot.size.x * fraction
            drawLine(Vector2(x, plot.position.y), Vector2(x, plot.end.y), gridColor)

            val xValue = xMin + (xMax - xMin) * fraction
            drawString(
                font,
                Vector2(x - 28.0, plot.end.y + 17.0),
                formatTick(xValue),
                HorizontalAlignment.CENTER,
                56.0f,
                fontSize,
                axisColor
            )
        }
        yTicks.forEach { yValue ->
            val fraction = (yMax - yValue) / (yMax - yMin)
            val y = plot.position.y + plot.size.y * fraction
            drawLine(Vector2(plot.position.x, y), Vector2(plot.end.x, y), gridColor)

            drawString(
                font,
                Vector2(2.0, y + 4.0),
                formatTick(yValue, integerYLabels),
                HorizontalAlignment.RIGHT,
                50.0f,
                fontSize,
                axisColor
            )
        }

        drawLine(plot.position, Vector2(plot.position.x, plot.end.y), axisColor, 1.5f)
        drawLine(Vector2(plot.position.x, plot.end.y), plot.end, axisColor, 1.5f)
        drawString(
            font,
            Vector2(plot.position.x, size.y - 4.0),
            xLabel,
            HorizontalAlignment.CENTER,
            plot.size.x.toFloat(),
            fontSize,
            axisColor
        )
        if (yLabel.isNotEmpty()) {
            drawString(font, Vector2(plot.position.x, 16.0), yLabel, modulate = axisColor, fontSize = fontSize)
        }

        val maxRenderedPoints = (plot.size.x * MAX_POINTS_PER_HORIZONTAL_PIXEL).toInt().coerceAtLeast(4)
        series.forEach { graphSeries ->
            val renderedPoints = downsampleMinMax(graphSeries.points, xMin, xMax, maxRenderedPoints).map { point ->
                pointToPlot(point, plot)
            }
            if (renderedPoints.size >= 2) {
                drawPolyline(PackedVector2Array(renderedPoints), graphSeries.color, 2.0f, true)
            } else if (renderedPoints.size == 1) {
                drawCircle(renderedPoints.single(), INSPECTION_POINT_RADIUS, graphSeries.color)
            }
        }

        val inspectionTime = pinnedX ?: hoveredX
        if (interactive && inspectionTime != null && inspectionTime in xMin..xMax) {
            val inspectionX = plot.position.x + (inspectionTime - xMin) / (xMax - xMin) * plot.size.x
            drawLine(
                Vector2(inspectionX, plot.position.y),
                Vector2(inspectionX, plot.end.y),
                Color(1.0, 1.0, 1.0, 0.55),
                1.0f,
            )
            inspectionsAt(inspectionTime).forEach { inspection ->
                drawCircle(pointToPlot(inspection.point, plot), INSPECTION_POINT_RADIUS, inspection.series.color)
            }
        }
    }

    private fun pointToPlot(point: Vector2, plot: Rect2) = Vector2(
        plot.position.x + (point.x - xMin) / (xMax - xMin) * plot.size.x,
        plot.end.y - (point.y.coerceIn(yMin, yMax) - yMin) / (yMax - yMin) * plot.size.y,
    )

    private fun formatTick(value: Double, integer: Boolean = false): String = when {
        integer || abs(value) >= 1000.0 -> String.format("%.0f", value)
        else -> String.format("%.1f", value)
    }

    companion object {
        private const val MAX_POINTS_PER_HORIZONTAL_PIXEL = 2.0
        private const val FOLLOW_LATEST_TOLERANCE = 0.01
        private const val ZOOM_IN = 0.8
        private const val ZOOM_OUT = 1.25
        private const val MAX_ZOOM_DIVISOR = 100_000.0
        private const val MINIMUM_X_SPAN = 1e-9
        private const val Y_PADDING = 0.08
        private const val CLICK_DRAG_THRESHOLD = 4.0
        private const val INSPECTION_POINT_RADIUS = 4.0f
    }
}

internal fun downsampleMinMax(
    points: List<Vector2>,
    xMin: Double,
    xMax: Double,
    maxPoints: Int,
): List<Vector2> {
    if (points.isEmpty() || xMax < xMin) return emptyList()

    val fromIndex = points.lowerBound(xMin)
    val toIndex = points.upperBound(xMax)
    val visibleCount = toIndex - fromIndex
    if (visibleCount <= 0) return emptyList()
    if (visibleCount <= maxPoints) return points.subList(fromIndex, toIndex)

    val bucketCount = (maxPoints / POINTS_PER_BUCKET).coerceAtLeast(1)
    val result = ArrayList<Vector2>(bucketCount * POINTS_PER_BUCKET)
    var bucketStart = fromIndex
    repeat(bucketCount) { bucketIndex ->
        val bucketEnd = fromIndex + (visibleCount.toLong() * (bucketIndex + 1) / bucketCount).toInt()
        if (bucketEnd <= bucketStart) return@repeat

        var minIndex = bucketStart
        var maxIndex = bucketStart
        for (index in bucketStart + 1..<bucketEnd) {
            if (points[index].y < points[minIndex].y) minIndex = index
            if (points[index].y > points[maxIndex].y) maxIndex = index
        }
        val firstExtreme = minOf(minIndex, maxIndex)
        val secondExtreme = maxOf(minIndex, maxIndex)
        result.add(points[bucketStart])
        if (firstExtreme != bucketStart) result.add(points[firstExtreme])
        if (secondExtreme != bucketStart && secondExtreme != firstExtreme) result.add(points[secondExtreme])
        val lastIndex = bucketEnd - 1
        if (lastIndex != bucketStart && lastIndex != firstExtreme && lastIndex != secondExtreme) {
            result.add(points[lastIndex])
        }
        bucketStart = bucketEnd
    }
    return result
}

private fun List<Vector2>.lowerBound(x: Double): Int {
    var low = 0
    var high = size
    while (low < high) {
        val middle = (low + high) ushr 1
        if (this[middle].x < x) low = middle + 1 else high = middle
    }
    return low
}

private fun List<Vector2>.upperBound(x: Double): Int {
    var low = 0
    var high = size
    while (low < high) {
        val middle = (low + high) ushr 1
        if (this[middle].x <= x) low = middle + 1 else high = middle
    }
    return low
}

private fun List<Vector2>.nearestTo(x: Double): Vector2? {
    if (isEmpty()) return null
    val upperIndex = lowerBound(x)
    if (upperIndex == 0) return first()
    if (upperIndex == size) return last()
    val lower = this[upperIndex - 1]
    val upper = this[upperIndex]
    return if (x - lower.x <= upper.x - x) lower else upper
}

private const val POINTS_PER_BUCKET = 4
