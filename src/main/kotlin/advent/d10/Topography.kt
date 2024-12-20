package advent.d10

import java.io.File

data class Point(val x: Int, val y: Int)

fun main() {
    val lines = File("src/main/kotlin/advent/d10/t2.txt").readLines()

    val map = parseToMap(lines)

    val allNines = mutableListOf<Point>()
    val zeros = map[0]

    for (zero in zeros!!) {
        val foundNines = findAllNinesFromZero(map, zero)
        allNines.addAll(foundNines)
    }
    println("All distinct nines reachable from all zeros: $allNines")
    println("Count of distinct nines: ${allNines.size}")
}

private fun findAllNinesFromZero(
    map: Map<Int, List<Point>>,
    point: Point,
    currentNum: Int = 0,
    visited: MutableSet<Pair<Int, Point>> = mutableSetOf()
): List<Point> {
    println("CurrentNum: $currentNum, Point: $point")
    if (!visited.add(Pair(currentNum, point))) {
        return emptyList()
    }
    if (currentNum == 9) {
        return listOf(point)
    }
    val nextPoints = map[currentNum + 1]?.toSet() ?: emptySet()
    println("NextPoints: $nextPoints")

    val results = mutableListOf<Point>()
    for (d in DIRECTIONS) {
        val neighbor = Point(point.x + d.x, point.y + d.y)
        if (neighbor in nextPoints) {
            println("Neighbor: $neighbor")
            results += findAllNinesFromZero(map, neighbor, currentNum + 1, visited)
        }
    }
    return results
}

private fun parseToMap(area: List<String>): Map<Int, List<Point>> {
    val result = mutableMapOf<Int, MutableList<Point>>()
    for ((y, line) in area.withIndex()) {
        for ((x, char) in line.withIndex()) {
            val digit = char.digitToInt()
            val positions = result.getOrPut(digit) { mutableListOf() }
            positions.add(Point(x, y))
        }
    }
    return result
}