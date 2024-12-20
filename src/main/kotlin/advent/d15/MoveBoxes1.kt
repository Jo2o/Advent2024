package advent.d15

import java.io.File

data class Item(val x: Int, val y: Int, val ch: Char)

fun main() {

    val areaLines = File("src/main/kotlin/advent/d15/i11.txt").readLines()
    val moves = File("src/main/kotlin/advent/d15/i12.txt").readLines().joinToString("")

    var area = parseArea(areaLines)

    for (move in moves) {
        area = move(move, area)
    }

    println(countGps(area))
}

private fun countGps(area: List<List<Item>>): Int {
    return area
        .flatten()
        .filter { it.ch == 'O' }
        .sumOf { it.x + it.y * 100 }
}

private fun move(moveSign: Char, area: List<List<Item>>): List<List<Item>> {
    println(moveSign)
    val robot = findRobot(area)
    var modifiedArea = area.map { it.toMutableList() }.toMutableList()
    val (dx, dy) = direction(moveSign)
    val nextPos = area[robot.y + dy][robot.x + dx]
    when (nextPos.ch) {
        '.' -> {
            modifiedArea[robot.y][robot.x] = Item(robot.x, robot.y, '.')
            modifiedArea[nextPos.y][nextPos.x] = Item(nextPos.x, nextPos.y, '@')
        }
        'O' -> {
            val freeSpace = findFreeSpace(robot, Pair(dx, dy), area)
            if (freeSpace != null) {
                modifiedArea = push(robot, freeSpace, Pair(dx, dy), area)
            }
        }
    }
    printArea(modifiedArea)
    return modifiedArea
}

private fun push(robot: Item, freeSpace: Item, dir: Pair<Int, Int>, area: List<List<Item>>): MutableList<MutableList<Item>> {
    val modifiedArea = area.map { it.toMutableList() }.toMutableList()
    modifiedArea[robot.y][robot.x] = Item(robot.x, robot.y, '.')
    modifiedArea[robot.y + dir.second][robot.x + dir.first] = Item(robot.x + dir.first, robot.y + dir.second, '@')
    modifiedArea[freeSpace.y][freeSpace.x] = Item(freeSpace.x, freeSpace.y, 'O')

    return modifiedArea
}

private fun findFreeSpace(robot: Item, dir: Pair<Int, Int>, area: List<List<Item>>): Item? {
    for (i in 1..50) {
        val nextX = dir.first * i
        val nextY = dir.second * i
        val nextPos = area[robot.y + nextY][robot.x + nextX]
        if (nextPos.ch == '.') {
            return nextPos
        } else if (nextPos.ch == '#') {
            return null
        }
    }
    return null
}


private fun direction(ch: Char): Pair<Int, Int> =
    when (ch) {
        '^' -> Pair(0, -1)
        'v' -> Pair(0, 1)
        '<' -> Pair(-1, 0)
        '>' -> Pair(1, 0)
        else -> Pair(0, 0)
    }

private fun findRobot(area: List<List<Item>>): Item {
    return area.flatten().first { it.ch == '@' }
}

private fun parseArea(lines: List<String>): List<List<Item>> {
    val result = mutableListOf<MutableList<Item>>()
    for (i in lines.indices) {
        val line = lines[i]
        val row = mutableListOf<Item>()
        for (j in line.indices) {
            row.add(Item(j, i, line[j]))
        }
        result.add(row)
    }
    return result
}

private fun printArea(area: List<List<Item>>) {
    for (y in area.indices) {
        for (x in 0 until area[0].size) {
            print(area[y][x].ch)
        }
        println()
    }
    println()
}