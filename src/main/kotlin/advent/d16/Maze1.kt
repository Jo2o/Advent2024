package advent.d16

import java.io.File
import java.util.*

//data class Position(val row: Int, val col: Int)
data class Point(val row: Int, val col: Int)
//data class State(val position: Position, val path: List<Position>)

private val DIRECTIONS = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))

fun main() {
    val maze = File("src/main/kotlin/advent/d16/i1.txt").readLines()
    val (start, end) = findStartAndEnd(maze)
    val allPaths = mutableListOf<List<Point>>()
    val visited = Array(maze.size) { BooleanArray(maze[0].length) { false } }


    dfsAllPaths(maze, start, end, visited, mutableListOf(), allPaths)

    var minScore = Int.MAX_VALUE
    for (p in allPaths) {
        printMazeWithPath(maze, p, start, end)
        val scoreSteps = p.size - 1
        val scoreTurns = countTurns(p) * 1000
        val score = scoreSteps + scoreTurns
        println("Score: $score")
        if (score < minScore) {
            minScore = score
        }
    }
    println(minScore)
}

fun dfsAllPaths(
    maze: List<String>,
    current: Point,
    end: Point,
    visited: Array<BooleanArray>,
    path: MutableList<Point>,
    allPaths: MutableList<List<Point>>
) {
    // Mark current as visited and add to path
    visited[current.row][current.col] = true
    path.add(current)

    // Check if we reached the end
    if (current == end) {
        // Make a copy of the current path and add to allPaths
        allPaths.add(ArrayList(path))
    } else {
        // Explore neighbors
        val directions = listOf(Point(1,0), Point(-1,0), Point(0,1), Point(0,-1))
        for (d in directions) {
            val next = Point(current.row + d.row, current.col + d.col)
            if (canMove(maze, visited, next)) {
                dfsAllPaths(maze, next, end, visited, path, allPaths)
            }
        }
    }

    // Backtrack
    path.removeAt(path.size - 1)
    visited[current.row][current.col] = false
}

fun canMove(maze: List<String>, visited: Array<BooleanArray>, p: Point): Boolean {
    val rows = maze.size
    val cols = maze[0].length
    if (p.row !in 0 until rows || p.col !in 0 until cols) return false
    if (maze[p.row][p.col] == '#') return false
    if (visited[p.row][p.col]) return false
    return true
}

fun printMazeWithPath(maze: List<String>, path: List<Point>, start: Point, end: Point) {
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