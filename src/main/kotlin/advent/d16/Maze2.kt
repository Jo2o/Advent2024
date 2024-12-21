package advent.d16

import java.io.File
import java.util.*

private const val N = 100

fun main() {
    val maze = File("src/main/kotlin/advent/d16/i1.txt").readLines()
    val (start, end) = findStartAndEnd(maze)
    val shortestPaths = findNShortestPaths(maze, start, end, N)

    val scoredPaths = mutableMapOf<Int, List<List<Point>>>()

    var minScore = Int.MAX_VALUE
    for (p in shortestPaths) {
        printMazeWithPath(maze, p.path, start, end)
        val scoreSteps = p.path.size - 1
        val scoreTurns = countTurns(p.path) * 1000
        val score = scoreSteps + scoreTurns
        println("Score: $score")
        if (score <= minScore) {
            minScore = score
            scoredPaths[score] = (scoredPaths[score]?.plus(listOf(p.path)) ?: listOf(p.path))
        }
    }
    println(minScore)

    val bestPathsPoints = mutableSetOf<Point>()
    for (path in scoredPaths[minScore]!!) {
        for (point in path) {
            bestPathsPoints.add(point)
        }
    }
    println("Best path points size: ${bestPathsPoints.size}")
}

private fun findNShortestPaths(maze: List<String>, start: Point, end: Point, n: Int): List<State> {
    // min-heap by score = steps + 1000*turns
    val pq = PriorityQueue<State>(compareBy { it.steps + 1000 * it.turns })
    pq.add(State(start, listOf(start), steps = 0, turns = 0, direction = null))

    // keep track of the best score found so far for a cell + direction.
    // key: (row, col, dirRow, dirCol) -> bestScore
    val visitedScores = mutableMapOf<Triple<Int, Int, Pair<Int, Int>?>, Int>()

    val foundPaths = mutableListOf<State>()

    while (pq.isNotEmpty() && foundPaths.size < n) {
        val current = pq.poll()
        val (cPoint, cPath, cSteps, cTurns, cDir) = current

        // If we reached the end, record this path
        if (cPoint == end) {
            foundPaths.add(current)
            continue
        }

        for (d in DIRECTIONS) {
            val next = Point(cPoint.row + d.first, cPoint.col + d.second)
            if (!canMove(maze, next)) continue

            val newSteps = cSteps + 1
            val newTurns = if (cDir == null) {
                // first move doesn't count as a turn, but let's decide how to handle:
                // We can start counting direction changes from the second step.
                // Let's assume no turn on the first step.
                cTurns
            } else {
                // If direction changed
                if (cDir != d) cTurns + 1 else cTurns
            }
            val newScore = newSteps + 1000 * newTurns

            val stateKey = Triple(next.row, next.col, d)
            val prevBest = visitedScores[stateKey]
            if (prevBest == null || newScore <= prevBest) {
                visitedScores[stateKey] = newScore
                pq.add(State(next, cPath + next, newSteps, newTurns, d))
            }
        }
    }
    return foundPaths
}

private fun canMove(maze: List<String>, p: Point): Boolean {
    val rows = maze.size
    val cols = maze[0].length
    if (p.row !in 0 until rows || p.col !in 0 until cols) return false
    if (maze[p.row][p.col] == '#') return false
    return true
}

private fun printMazeWithPath(maze: List<String>, path: List<Point>, start: Point, end: Point) {
    val mazeChars = maze.map { it.toCharArray() }
    for (p in path) {
        if (p != start && p != end) {
            mazeChars[p.row][p.col] = '▢'
        }
    }
    for (row in mazeChars) {
        println(row.joinToString(""))
    }
}

private fun countTurns(path: List<Point>): Int {
    var result = 0
    var horizontally = path[0].row == path[1].row
    var vertically = path[0].col == path[1].col
    if (vertically) result++
    for (i in 1 until path.size) {
        if (path[i-1].col == path[i].col && vertically) {
            continue
        }
        if (path[i-1].row == path[i].row && horizontally) {
            continue
        }
        horizontally = !horizontally
        vertically = !vertically
        result++
    }
    return result
}

private fun findStartAndEnd(mazeLines: List<String>): Pair<Point, Point> {
    var start: Point? = null
    var end: Point? = null
    for (i in mazeLines.indices) {
        for (j in mazeLines[i].indices) {
            when (mazeLines[i][j]) {
                'S' -> start = Point(i, j)
                'E' -> end = Point(i, j)
            }
        }
    }
    return Pair(start!!, end!!)
}