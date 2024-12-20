package advent.d8

import java.io.File

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
            val resonantPoints = calculateResonantPoints(p1, p2, areaW, areaH)
            println("$p1, $p2 -> $resonantPoints")
            resonantPoints.forEach { antiNodes.add(it) }
        }

    }
    println("Antinodes with resonants count: ${antiNodes.size}")

}

private fun calculateResonantPoints(p1: Point, p2: Point, areaW: Int, areaH: Int): Set<Point> {
    val result = mutableSetOf<Point>()
    result.add(p1)
    result.add(p2)
    val dx = p2.x - p1.x
    val dy = p2.y - p1.y

    var currentX = p2.x
    var currentY = p2.y
    while (currentX + dx in 0 until areaW && currentY + dy in 0 until areaH) {
        currentX += dx
        currentY += dy
        result.add(Point(currentX, currentY))
    }

    currentX = p1.x
    currentY = p1.y
    while (currentX - dx in 0 until areaW && currentY - dy in 0 until areaH) {
        currentX -= dx
        currentY -= dy
        result.add(Point(currentX, currentY))
    }
    return result
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
            if (char != '.') {
                charMap.computeIfAbsent(char) { mutableListOf() }
                    .add(Point(x, y))
            }
        }
    }
    return charMap.mapValues { it.value.toList() }
}