package advent.d12

import java.io.File
import java.util.*

val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

data class Pos(
    val x: Int,
    val y: Int,
    val groupId: Int = -1,
    val visited: Boolean = false,
)

class Group(
    val groupId: Int,
    val groupLetter: Char,
    val items: MutableList<Pos> = mutableListOf(),
) {
    fun add(pos: Pos) {
        items.add(pos)
    }
    override fun toString(): String {
        return "G{$groupId,'$groupLetter'}:\n  its=$items"
    }
}

fun main() {
    val area = File("src/main/kotlin/advent/d12/i1.txt").readLines()

    val groups = getGroups(area)

    var total = 0
    for (group in groups) {
        println(group)
        val freeSides = calculateFreeSides(group, area)
        total += (freeSides * group.items.size)
    }
    println("Total: $total")
}

private fun calculateFreeSides(group: Group, area: List<String>): Int {
    val rows = area.size
    val cols = area[0].length
    val grid = Array(rows) { r -> area[r].toCharArray() }
    var freeSides = 0
    for (pos in group.items) {
        val (x, y) = pos
        for ((dx, dy) in directions) {
            val nx = x + dx
            val ny = y + dy
            if (nx in 0 until rows && ny in 0 until cols) {
                if (grid[nx][ny] != group.groupLetter) {
                    freeSides++
                }
            } else {
                freeSides++
            }
        }
    }
    return freeSides
}

private fun getGroups(lines: List<String>): List<Group> {
    val rows = lines.size
    val cols = lines[0].length
    val grid = Array(rows) { r -> lines[r].toCharArray() }
    val visited = Array(rows) { BooleanArray(cols) { false } }

    val groups = mutableListOf<Group>()
    var currentGroupId = 1

    for (x in 0 until rows) {
        for (y in 0 until cols) {
            if (!visited[x][y]) {
                val newGroup = Group(currentGroupId, grid[x][y])
                val charToMatch = grid[x][y]

                val queue: Queue<Pair<Int, Int>> = LinkedList()
                queue.add(x to y)
                println("New Q: $queue")
                visited[x][y] = true

                while (queue.isNotEmpty()) {
                    val (cx, cy) = queue.remove()
                    println("Removed Q: $queue -> [$cx, $cy]")
                    newGroup.add(Pos(cx, cy, currentGroupId))
                    for ((dx, dy) in directions) {
                        val nx = cx + dx
                        val ny = cy + dy
                        if (nx in 0 until rows && ny in 0 until cols && !visited[nx][ny]) {
                            if (grid[nx][ny] == charToMatch) {
                                visited[nx][ny] = true
                                queue.add(nx to ny)
                                println("Added Q: $queue")
                            }
                        }
                    }
                }
                groups.add(newGroup)
                currentGroupId++
            }
        }
    }
    return groups
}