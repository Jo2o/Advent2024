package advent.d6

import java.io.File

enum class DIRECTION { LEFT, RIGHT, UP, DOWN }
enum class LIMIT { FREE, OBSTACLE, OUT_OF_AREA }
data class Position(val x: Int, val y: Int, val obj: Char) {
    override fun toString(): String {
        return "($x, $y, $obj)"
    }
}

fun main() {
     val mapStr = File("src/main/kotlin/advent/d6/in1.txt").readLines()
//    val mapStr = File("src/main/kotlin/advent/d6/t1.txt").readLines()
    val mapSize = mapStr.size

    val area = mutableListOf<Position>()
    for (i in mapStr.indices) {
        val row = mapStr[i]
        for (j in row.indices) area.add(Position(j, i, row[j]))
    }
    val reindexedArea = reindexArea(area, mapSize - 1)

    val guardVisitedPositions = mutableSetOf<Position>()
    var guardPosition = findGuard(reindexedArea)
    var guardPositionCounter = 0
    var nextLimit = LIMIT.FREE
    var direction = DIRECTION.UP
    var moveResult: Pair<Position, LIMIT>

    while (nextLimit != LIMIT.OUT_OF_AREA) {
        if (nextLimit == LIMIT.FREE) {
            guardVisitedPositions.add(guardPosition)

            if (direction == DIRECTION.UP) {
                moveResult = moveUpBy1(guardPosition, reindexedArea, mapSize)
                guardPosition = moveResult.first
                nextLimit = moveResult.second
            } else if (direction == DIRECTION.DOWN) {
                moveResult = moveDownBy1(guardPosition, reindexedArea)
                guardPosition = moveResult.first
                nextLimit = moveResult.second
            } else if (direction == DIRECTION.LEFT) {
                moveResult = moveLeftBy1(guardPosition, reindexedArea)
                guardPosition = moveResult.first
                nextLimit = moveResult.second
            } else if (direction == DIRECTION.RIGHT) {
                moveResult = moveRightBy1(guardPosition, reindexedArea, mapSize)
                guardPosition = moveResult.first
                nextLimit = moveResult.second
            }

            guardPositionCounter++
        }
        if (nextLimit == LIMIT.OBSTACLE) {
            guardPositionCounter--
            direction = turnRight(direction)
            nextLimit = LIMIT.FREE
            println("Obstacle at ${guardPosition.x}, ${guardPosition.y}")
        }
    }

    println(guardPositionCounter)
    println(guardVisitedPositions.size)
}

private fun turnRight(currentDirection: DIRECTION): DIRECTION {
    return when (currentDirection) {
        DIRECTION.UP -> DIRECTION.RIGHT
        DIRECTION.DOWN -> DIRECTION.LEFT
        DIRECTION.RIGHT -> DIRECTION.DOWN
        DIRECTION.LEFT -> DIRECTION.UP
    }
}

private fun moveUpBy1(previousPosition: Position, area: List<Position>, areaHeight: Int): Pair<Position, LIMIT> {
    val nextY = previousPosition.y + 1
    if (nextY == areaHeight) return Pair(previousPosition, LIMIT.OUT_OF_AREA)
    val nextPosition = area.find { it.y == nextY && it.x == previousPosition.x }!!
    if (nextPosition.obj != '#') {
        return Pair(nextPosition, LIMIT.FREE)
    }
    return Pair(previousPosition, LIMIT.OBSTACLE)
}

private fun moveDownBy1(previousPosition: Position, area: List<Position>): Pair<Position, LIMIT> {
    val nextY = previousPosition.y - 1
    if (nextY < 0) return Pair(previousPosition, LIMIT.OUT_OF_AREA)
    val nextPosition = area.find { it.y == nextY && it.x == previousPosition.x }!!
    if (nextPosition.obj != '#') {
        return Pair(nextPosition, LIMIT.FREE)
    }
    return Pair(previousPosition, LIMIT.OBSTACLE)
}

private fun moveLeftBy1(previousPosition: Position, area: List<Position>): Pair<Position, LIMIT> {
    val nextX = previousPosition.x - 1
    if (nextX < 0) return Pair(previousPosition, LIMIT.OUT_OF_AREA)
    val nextPosition = area.find { it.x == nextX && it.y == previousPosition.y }!!
    if (nextPosition.obj != '#') {
        return Pair(nextPosition, LIMIT.FREE)
    }
    return Pair(previousPosition, LIMIT.OBSTACLE)
}

private fun moveRightBy1(previousPosition: Position, area: List<Position>, areaWidth: Int): Pair<Position, LIMIT> {
    val nextX = previousPosition.x + 1
    if (nextX == areaWidth) return Pair(previousPosition, LIMIT.OUT_OF_AREA) // it's a square
    val nextPosition = area.find { it.x == nextX && it.y == previousPosition.y }!!
    if (nextPosition.obj != '#') {
        return Pair(nextPosition, LIMIT.FREE)
    }
    return Pair(previousPosition, LIMIT.OBSTACLE)
}

private fun reindexArea(area: List<Position>, maxY: Int): List<Position> {
    return area.map { Position(it.x, maxY - it.y, it.obj) }
}

private fun findGuard(area: List<Position>): Position {
    return area.find { it.obj != '.' && it.obj != '#' }!!
}