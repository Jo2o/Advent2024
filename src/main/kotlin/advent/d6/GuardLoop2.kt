package advent.d6

import java.io.File

fun main() {
     val areaStr = File("src/main/kotlin/advent/d6/in1.txt").readLines()
//    val areaStr = File("src/main/kotlin/advent/d6/t5.txt").readLines()
    val areaSize = areaStr.size

    val area = mutableListOf<Position>()
    for (i in areaStr.indices) {
        val row = areaStr[i]
        for (j in row.indices) area.add(Position(j, i, row[j]))
    }
    val reindexedArea = reindexArea(area, areaSize - 1)

    val guardVisitedPositions = mutableSetOf<Position>()
    val newObstaclePositions = mutableSetOf<Position>()
    val origGuardPosition = findGuard(reindexedArea)
    var guardPosition = origGuardPosition
    var nextLimit = LIMIT.FREE
    var direction = DIRECTION.UP

    while (nextLimit != LIMIT.OUT_OF_AREA) {
        if (nextLimit == LIMIT.FREE) {
            guardVisitedPositions.add(guardPosition)
            println("ML Guard at [${guardPosition.x}, ${guardPosition.y}] - $direction - $nextLimit")

            val result = go(direction, guardPosition, reindexedArea, areaSize)
            guardPosition = result.first
            nextLimit = result.second
//            println("ML after GO [${guardPosition.x}, ${guardPosition.y}] - $direction - $nextLimit")

            if (nextLimit != LIMIT.OBSTACLE && checkLoop(direction, guardPosition, reindexedArea, areaSize, guardVisitedPositions)) {
                newObstaclePositions.add(guardPosition)
            }
        }
        if (nextLimit == LIMIT.OBSTACLE) {
//            println("Obstacle coming ${guardPosition.x}, ${guardPosition.y}, $direction")
            direction = turnRight(direction)
            nextLimit = LIMIT.FREE
        }
    }
    println(guardVisitedPositions.size)
    println("NewObstaclePositions: ${newObstaclePositions.size}")
}

private fun checkLoop(
    direction: DIRECTION,
    guardPosition: Position,
    area: List<Position>,
    areaSize: Int,
    walkedPath: Set<Position>,
): Boolean {
    val stepBackPosition = stepBack(guardPosition, direction)
    println("Checking loop at ${guardPosition.x}, ${guardPosition.y} - $direction")
    if (walkedPath.contains(guardPosition)) {
        return false
    }

    val modifiedArea = putObstacle(guardPosition, area)
    val guardVisitedPositions = mutableListOf<Pair<Position, DIRECTION>>()

    var guardPos = stepBackPosition
    var dir = direction
    var nextLimit = LIMIT.FREE

    var previousPosDir = Pair(Position(-1,-1,'.'), dir)

    while (nextLimit != LIMIT.OUT_OF_AREA) {
        if (nextLimit == LIMIT.FREE) {
//            println("CHL before GO [${guardPos.x}, ${guardPos.y}] - $dir - $nextLimit")
            previousPosDir = Pair(guardPos, dir)
            val result = go(dir, guardPos, modifiedArea, areaSize)
            guardPos = result.first
            nextLimit = result.second
//            println("CHL after GO [${guardPos.x}, ${guardPos.y}] - $dir - $nextLimit")
        }
        if (nextLimit == LIMIT.OBSTACLE) {
            dir = turnRight(dir)
            nextLimit = getNextLimit(guardPos, dir, modifiedArea, areaSize)
        }
        if (nextLimit != LIMIT.OBSTACLE && guardVisitedPositions.contains(Pair(guardPos, dir))) {
            println("Returned Loop detected at ${guardPosition.x}, ${guardPosition.y} - $direction")
             return true
        }
        guardVisitedPositions.add(previousPosDir)
    }
    return false
}

private fun getNextLimit(position: Position, direction: DIRECTION, area: List<Position>, areaSize: Int): LIMIT {
    return when (direction) {
        DIRECTION.UP -> moveUpBy1(position, area, areaSize).second
        DIRECTION.DOWN -> moveDownBy1(position, area).second
        DIRECTION.LEFT -> moveLeftBy1(position, area).second
        DIRECTION.RIGHT -> moveRightBy1(position, area, areaSize).second
    }
}

private fun putObstacle(position: Position, area: List<Position>) =
    area.map { if (it == position) it.copy(obj = '#') else it }

private fun stepBack(position: Position, direction: DIRECTION) =
    when (direction) {
        DIRECTION.UP -> Position(position.x, position.y - 1, '.')
        DIRECTION.DOWN -> Position(position.x, position.y + 1, '.')
        DIRECTION.LEFT -> Position(position.x + 1, position.y, '.')
        DIRECTION.RIGHT -> Position(position.x - 1, position.y, '.')
    }

private fun go(
    direction: DIRECTION,
    guardPosition: Position,
    reindexedArea: List<Position>,
    mapSize: Int,
): Pair<Position, LIMIT> {
    return when (direction) {
        DIRECTION.UP -> moveUpBy1(guardPosition, reindexedArea, mapSize)
        DIRECTION.DOWN -> moveDownBy1(guardPosition, reindexedArea)
        DIRECTION.LEFT -> moveLeftBy1(guardPosition, reindexedArea)
        DIRECTION.RIGHT -> moveRightBy1(guardPosition, reindexedArea, mapSize)
    }
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

private fun reindexArea(area: List<Position>, maxY: Int) =
    area.map { Position(it.x, maxY - it.y, it.obj) }

private fun findGuard(area: List<Position>) =
    area.find { it.obj != '.' && it.obj != '#' }!!