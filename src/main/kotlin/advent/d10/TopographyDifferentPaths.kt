package advent.d10

import java.io.File

val DIRECTIONS = listOf(
    Point(0, -1), // up
    Point(0, 1),  // down
    Point(-1, 0), // left
    Point(1, 0)   // right
)

fun main() {
    val lines = File("src/main/kotlin/advent/d10/i1.txt").readLines()

    val map = parseToMap(lines)

    var pathsCount = 0

    val zeros = map[0]
    for (zero in zeros!!) {
        pathsCount += countPathsToNine(map, zero)
    }
    println("pathsCount: ${pathsCount}")
}

private fun countPathsToNine(
    map: Map<Int, List<Point>>,
    point: Point,
    currentNum: Int = 0,
    visited: MutableSet<Pair<Int, Point>> = mutableSetOf()
): Int {
    if (!visited.add(Pair(currentNum, point))) return 0

    if (currentNum == 9) {
        visited.remove(Pair(currentNum, point))
        return 1
    }

    val nextPoints = map[currentNum + 1]?.toSet() ?: emptySet()

    var pathCount = 0
    for (d in DIRECTIONS) {
        val neighbor = Point(point.x + d.x, point.y + d.y)
        if (neighbor in nextPoints) {
            pathCount += countPathsToNine(map, neighbor, currentNum + 1, visited)
        }
    }
    visited.remove(Pair(currentNum, point))
    return pathCount
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