package advent.d8

import java.io.File

data class Point(val x: Int, val y: Int)

fun main() {
    val inStr = File("src/main/kotlin/advent/d8/i1.txt").readLines()

    val map = parseToMap(inStr)
    val areaW = inStr[0].length
    val areaH = inStr.size

    val antiNodes = mutableSetOf<Point>()

    for ((k, v) in map) {
        println("$k: $v")
        if (v.size == 1) continue
        val pairs = generateUniquePairs(v)
        for ((p1, p2) in pairs) {
            val (extP1, extP2) = calculateExtendedPoints(p1, p2)
            println("$p1, $p2 -> $extP1, $extP2")
            if (isInArea(extP1, areaW, areaH)) {
                antiNodes.add(extP1)
            }
            if (isInArea(extP2, areaW, areaH)) {
                antiNodes.add(extP2)
            }
        }

    }
    println("Antinodes count: ${antiNodes.size}")

}

private fun isInArea(p: Point, areaW: Int, areaH: Int): Boolean {
    return p.x in 0 until areaW && p.y in 0 until areaH
}

private fun calculateExtendedPoints(p1: Point, p2: Point): Pair<Point, Point> {
    val dx = p2.x - p1.x
    val dy = p2.y - p1.y
    val extendedPoint1 = Point(p1.x - dx, p1.y - dy) // negative direction
    val extendedPoint2 = Point(p2.x + dx, p2.y + dy) // positive direction
    return Pair(extendedPoint1, extendedPoint2)
}

private fun generateUniquePairs(points: List<Point>): List<Pair<Point, Point>> {
    val pairs = mutableListOf<Pair<Point, Point>>()
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            pairs.add(Pair(points[i], points[j]))
        }
    }
    return pairs
}

private fun parseToMap(lines: List<String>): Map<Char, List<Point>> {
    val charMap = mutableMapOf<Char, MutableList<Point>>()
    for (y in lines.indices) {
        for (x in lines[y].indices) {
            val char = lines[y][x]
            if (char != '.') { // Ignore empty spaces
                charMap.computeIfAbsent(char) { mutableListOf() }
                    .add(Point(x, y))
            }
        }
    }
    return charMap.mapValues { it.value.toList() }
}