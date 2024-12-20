package advent.d15

import java.io.File

data class Direction(val x: Int, val y: Int)

fun main() {

    val areaLines = File("src/main/kotlin/advent/d15/i11.txt").readLines()
    val moves = File("src/main/kotlin/advent/d15/i12.txt").readLines().joinToString("")

    var area = parseArea(areaLines)
    printArea(area)

    var pauseCounter = 0
    for (move in moves) {
        area = move(move, area)
        pauseCounter++
    }

    println(countGps(area))
}

private fun countGps(area: List<List<Item>>): Int {
    return area
        .flatten()
        .filter { it.ch == '[' }
        .sumOf { it.x + it.y * 100 }
}

private fun move(moveSign: Char, area: List<List<Item>>): List<List<Item>> {
    println(moveSign)
    val robot = findRobot(area)
    var modifiedArea = area.map { it.toMutableList() }.toMutableList()
    val dir = direction(moveSign)
    val nextPos = area[robot.y + dir.y][robot.x + dir.x]
    when (nextPos.ch) {
        '.' -> {
            modifiedArea[robot.y][robot.x] = Item(robot.x, robot.y, '.')
            modifiedArea[nextPos.y][nextPos.x] = Item(nextPos.x, nextPos.y, '@')
        }
        '[', ']' -> {
            modifiedArea = pushGroup(robot, dir, modifiedArea)
        }
    }
    printArea(modifiedArea)
    return modifiedArea
}

private fun pushGroup(robot: Item, dir: Direction, area: MutableList<MutableList<Item>>): MutableList<MutableList<Item>> {
    val group = getGroup(robot, dir, area)
    return if (group.isMoveable(dir, area)) {
        val modifiedArea = moveGroup(group, dir, area)
        modifiedArea[robot.y][robot.x] = Item(robot.x, robot.y, '.')
        modifiedArea[robot.y + dir.y][robot.x + dir.x] = Item(robot.x + dir.x, robot.y + dir.y, '@')
        if (dir.x == 0) {
            val robo = findRobot(modifiedArea)
            val nextLeft = modifiedArea[robo.y][robo.x - 1]
            if (nextLeft.ch == '[') {
                modifiedArea[nextLeft.y][nextLeft.x] = Item(nextLeft.x, nextLeft.y, '.')
            }
            val nextRight = modifiedArea[robo.y][robo.x + 1]
            if (nextRight.ch == ']') {
                modifiedArea[nextRight.y][nextRight.x] = Item(nextRight.x, nextRight.y, '.')
            }
        }
        clearLeftoverHalves(modifiedArea)
    } else {
        area
    }
}

private fun clearLeftoverHalves(area: MutableList<MutableList<Item>>): MutableList<MutableList<Item>> {
    for (row in area) {
        for (item in row) {
            if (item.ch == '[' && area[item.y][item.x + 1].ch == '[') {
                area[item.y][item.x] = Item(item.x, item.y, '.')
            }
            if (item.ch == ']' && area[item.y][item.x + 1].ch == ']') {
                area[item.y][item.x+1] = Item(item.x+1, item.y, '.')
            }
        }
    }
    return area
}

private fun Set<Item>.isMoveable(dir: Direction, area: List<List<Item>>): Boolean {
    if (this.isEmpty()) return false
    return this.all { area[it.y + dir.y][it.x + dir.x].ch != '#' }
}

private fun getGroup(robot: Item, dir: Direction, area: List<List<Item>>): Set<Item> {
    return addIteratively(robot, dir, area)
}

private fun addIteratively(item: Item, dir: Direction, area: List<List<Item>>): Set<Item> {
    if (item.x + dir.x < 0 || item.x + dir.x >= area[0].size || item.y + dir.y < 0 || item.y + dir.y >= area.size) {
        return emptySet()
    }
    val nextItem = area[item.y + dir.y][item.x + dir.x]
    if (nextItem.ch != '[' && nextItem.ch != ']') {
        return emptySet()
    }
    val otherHalf = getOtherHalf(nextItem)
    if (dir.y == 0) {
        val nnItem = area[item.y][nextItem.x + dir.x]
        return setOf(nextItem, otherHalf) + addIteratively(nnItem, dir, area)
    }
    return setOf(nextItem, otherHalf) + addIteratively(nextItem, dir, area) + addIteratively(otherHalf, dir, area)
}

private fun moveGroup(group: Set<Item> , dir: Direction, area: List<List<Item>>): MutableList<MutableList<Item>> {
    val modifiedArea = area.map { it.toMutableList() }.toMutableList()
    for (g in group) {
        modifiedArea[g.y + dir.y][g.x + dir.x] = Item(g.x + dir.x, g.y + dir.y, g.ch)
    }

    return modifiedArea
}

private fun getOtherHalf(item: Item): Item {
    if (item.ch == '[') {
        return Item(item.x + 1, item.y, ']')
    } else if (item.ch == ']') {
        return Item(item.x - 1, item.y, '[')
    } else {
        throw IllegalArgumentException("Item is not: '[', ']'")
    }
}


private fun direction(ch: Char): Direction =
    when (ch) {
        '^' -> Direction(0, -1)
        'v' -> Direction(0, 1)
        '<' -> Direction(-1, 0)
        '>' -> Direction(1, 0)
        else -> Direction(0, 0)
    }

private fun findRobot(area: List<List<Item>>): Item {
    return area.flatten().first { it.ch == '@' }
}

private fun parseArea(lines: List<String>): List<List<Item>> {
    val result = mutableListOf<MutableList<Item>>()
    for (i in lines.indices) {
        val line = lines[i]
            .replace(".", "..")
            .replace("#", "##")
            .replace("O", "[]")
            .replace("@", "@.")
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