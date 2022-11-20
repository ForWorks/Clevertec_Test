import java.io.File
import java.io.FileWriter
import kotlin.math.pow
import kotlin.math.sqrt

fun main() {

    val lines = mutableListOf(
        Line(Point(15, 0), Point(15, 3210)),
        Line(Point(0, 15), Point(6000, 15)),
        Line(Point(1500, 0), Point(1500, 3210)),
        Line(Point(15, 1015), Point(1500, 1015)),
        Line(Point(15, 2015), Point(1500, 2015)),
        Line(Point(15, 3015), Point(1500, 3015)),
        Line(Point(2550, 0), Point(2550, 3210)),
        Line(Point(1500, 1415), Point(2550, 1415)),
        Line(Point(1500, 2815), Point(2550, 2815)),
        Line(Point(3991, 0), Point(3991, 3210)),
        Line(Point(2550, 515), Point(3991, 515)),
        Line(Point(2550, 1015), Point(3991, 1015)),
        Line(Point(2550, 1515), Point(3991, 1515)),
        Line(Point(2550, 2015), Point(3991, 2015)),
        Line(Point(2550, 2765), Point(3991, 2765)),
        Line(Point(3250, 2015), Point(3250, 2765)),
        Line(Point(4789, 0), Point(4789, 3210)),
        Line(Point(3991, 1515), Point(4789, 1515)),
        Line(Point(3991, 3015), Point(4789, 3015)),
        Line(Point(5843, 0), Point(5843, 3210)),
        Line(Point(4789, 1123), Point(5843, 1123)),
        Line(Point(5316, 15), Point(5316, 1123)),
    )

    val figures = listOf(
        Figure(Point(0, 0), Point(1470, 0), Point(1200, 1000), Point(0, 1000), 15, 15),
        Figure(Point(0, 0), Point(1470, 0), Point(1200, 1000), Point(0, 1000), 15, 1015),
        Figure(Point(15, 0), Point(1485, 0), Point(1485, 1000), Point(285, 1000), 15, 2015),
        Figure(Point(0, 0), Point(798, 0), Point(798, 1485), Point(0, 1000), 3991, 15),
        Figure(Point(0, 0), Point(798, 0), Point(798, 1200), Point(0, 1485), 3991, 1515),
        Figure(Point(15, 0), Point(685, 0), Point(600, 735), Point(150, 735), 2550, 2015),
    )

    val result = machineOptimization(lines, figures)

    val file = File("result.txt")
    FileWriter(file).use {
        result.forEach { line ->
            it.write(line.toString())
        }
    }
}

private fun machineOptimization(lines: MutableList<Line>, figures: List<Figure>): MutableList<Line> {

    figures.forEach {
        lines.add(getLineFromFigure(it, it.point1, it.point2))
        lines.add(getLineFromFigure(it, it.point2, it.point3))
        lines.add(getLineFromFigure(it, it.point3, it.point4))
        lines.add(getLineFromFigure(it, it.point4, it.point1))
    }

    removeIdenticalLines(lines)
    return pathOptimization(lines)
}

private fun getLineFromFigure(figure: Figure, point1: Point, point2: Point): Line {
    return Line(
        Point(point1.x + figure.positionX, point1.y + figure.positionY),
        Point(point2.x + figure.positionX, point2.y + figure.positionY)
    )
}

private fun removeIdenticalLines(lines: MutableList<Line>) {
    var i = 0
    while (i < lines.size - 1) {
        var j = i + 1
        val line1 = lines[i]
        while (j < lines.size) {
            val line2 = lines[j]
            if (isLinesOverlap(line1, line2, true)) {
                if (isFullLineIn(line1, line2, false)) {
                    lines.remove(line1)
                } else if (isFullLineIn(line2, line1, false)) {
                    lines.remove(line2)
                }
            }
            else if(isLinesOverlap(line1, line2, false)) {
                if (isFullLineIn(line1, line2, true)) {
                    lines.remove(line1)
                } else if (isFullLineIn(line2, line1, true)) {
                    lines.remove(line2)
                }
            }
            j++
        }
        i++
    }
}

private fun isLinesOverlap(line1: Line, line2: Line, xOrientation: Boolean): Boolean {
    if(xOrientation) {
        return line1.point1.x == line2.point1.x && line1.point2.x == line2.point2.x
    }
    return line1.point1.y == line2.point1.y && line1.point2.y == line2.point2.y
}

private fun isFullLineIn(line1: Line, line2: Line, xOrientation: Boolean): Boolean {
    if(xOrientation) {
        return line1.point1.x >= line2.point1.x && line1.point2.x <= line2.point2.x
    }
    return line1.point1.y >= line2.point1.y && line1.point2.y <= line2.point2.y
}

private fun pathOptimization(lines: MutableList<Line>): MutableList<Line> {
    var currentX = 0
    var currentY = 0
    val newLines = mutableListOf<Line>()
    for(i in 0 until lines.size) {
        var minDistance = 1000000.0
        var index = 0
        for(j in 0 until lines.size) {
            val distance = getMinDistance(currentX, currentY, lines[j])
            if(minDistance > distance) {
                minDistance = distance
                index = j
            }
        }
        val line = lines[index]
        if(line.startToEnd) {
            newLines.add(Line(line.point1, line.point2))
            currentX = line.point2.x
            currentY = line.point2.y
        }
        else {
            newLines.add(Line(line.point2, line.point1))
            currentX = line.point1.x
            currentY = line.point1.y
        }
        lines.remove(line)
    }
    return newLines
}

private fun getMinDistance(currentX: Int, currentY: Int, line: Line): Double {
    val distance1 = sqrt(
        (currentX - line.point1.x).toDouble().pow(2) +
                (currentY - line.point1.y).toDouble().pow(2)
    )
    val distance2 = sqrt(
        (currentX - line.point2.x).toDouble().pow(2) +
                (currentY - line.point2.y).toDouble().pow(2)
    )
    line.startToEnd = distance1 <= distance2
    if(distance1 <= distance2)
        return distance1
    return distance2
}

data class Point(val x: Int, val y: Int)

data class Figure(val point1: Point, val point2: Point, val point3: Point, val point4: Point, val positionX: Int, val positionY: Int)

data class Line(val point1: Point, val point2: Point, var startToEnd: Boolean = true) {
    override fun toString(): String {
        return "${point1.x}, ${point1.y}, ${point2.x}, ${point2.y}\n"
    }
}