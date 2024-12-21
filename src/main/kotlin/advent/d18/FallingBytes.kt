package advent.d18

import java.io.File

data class Point(val x: Int, val y: Int, var ch: Char)
val DIRECTIONS = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
private const val SIZE = 70
private const val BYTES = 1024

fun main() {
    val inStrs = File("src/main/kotlin/advent/d18/i1.txt").readLines()
    val map = createMap(inStrs)
    printMap(map)
    val path = findShortestPath(map)

    println("Steps: ${path?.size?.minus(1)}")
}

private fun findShortestPath( maze: List<Point>): List<Point>? {
    val pointMap = maze.associateBy { it.x to it.y }

    // Locate start and end points
    val start = maze.find { it.ch == 'S' } ?: return null
    val end = maze.find { it.ch == 'E' } ?: return null

    // BFS setup
    val queue = ArrayDeque<Pair<Point, List<Point>>>()
    val visited = mutableSetOf<Point>()

    queue.add(start to listOf(start))
    visited.add(start)

    while (queue.isNotEmpty()) {
        val (current, path) = queue.removeFirst()

        if (current == end) return path

        for ((dx, dy) in DIRECTIONS) {
            val neighbor = pointMap[current.x + dx to current.y + dy]

            if (neighbor != null && neighbor.ch != '#' && neighbor !in visited) {
                visited.add(neighbor)
                queue.add(neighbor to path + neighbor)
            }
        }
    }
    return null
}


private fun createMap(inStrs: List<String>): List<Point> {
    val map = mutableListOf<Point>()
    for (i in 0..SIZE) {
        for (j in 0..SIZE) {
            map.add(Point(i, j, '.'))
        }
    }
    updatePoint(map, 0, 0, 'S')
    updatePoint(map, SIZE, SIZE, 'E')
    var counter = 0
    for (line in inStrs) {
        if (counter == BYTES) {
            break
        }
        val nums = line.split(",").map { it.toInt() }
        updatePoint(map, nums[1], nums[0], '#')
        counter++
    }
    return map
}

private fun updatePoint(map: List<Point>, x: Int, y: Int, ch: Char) {
    map.find { it.x == x && it.y == y }?.ch = ch
}

private fun printMap(map: List<Point>) {
    for (i in 0..SIZE) {
        for (j in 0..SIZE) {
            print(map.find { it.x == i && it.y == j }?.ch)
        }
        println()
    }
}