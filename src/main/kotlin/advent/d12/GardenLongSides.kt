package advent.d12

import java.io.File
import java.util.*

data class FreeSideData(
    val x: Int,  // position
    val y: Int,
    val dx: Int, // direction
    val dy: Int,
    val nx: Int,
    val ny: Int,
)

fun main() {
    val area = File("src/main/kotlin/advent/d12/i1.txt").readLines()

    val groups = getGroups(area)

    var total = 0
    var totalLines = 0
    for (group in groups) {
        println(group)
        val (freeSides, freeSideData) = calculateFreeSides(group, area)
        total += (freeSides * group.items.size)
        totalLines += (countFreeSideLines(freeSideData) * group.items.size)
    }
    println("Total Lines: $totalLines")
}

private fun calculateFreeSides(group: Group, area: List<String>): Pair<Int, List<FreeSideData>> {
    val rows = area.size
    val cols = area[0].length
    val grid = Array(rows) { r -> area[r].toCharArray() }
    var freeSides = 0
    val freeSideDataList = mutableListOf<FreeSideData>()
    for (pos in group.items) {
        val (x, y) = pos
        for ((dx, dy) in directions) {
            val nx = x + dx
            val ny = y + dy
            if (nx in 0 until rows && ny in 0 until cols) {
                if (grid[nx][ny] != group.groupLetter) {
                    freeSides++
                    freeSideDataList.add(FreeSideData(x, y, dx, dy, nx, ny))
                }
            } else {
                freeSides++
                freeSideDataList.add(FreeSideData(x, y, dx, dy, nx, ny))
            }
        }
    }
    return Pair(freeSides, freeSideDataList)
}

private fun countFreeSideLines(freeSideDataList: List<FreeSideData>): Int {
    val groupedByDirection = freeSideDataList.groupBy { it.dx to it.dy }

    val fromUpOffset = groupAndSort(groupedByDirection[0 to -1].orEmpty(), { it.y }, { it.x })
    val fromDownOffset = groupAndSort(groupedByDirection[0 to 1].orEmpty(), { it.y }, { it.x })
    val fromLeftOffset = groupAndSort(groupedByDirection[1 to 0].orEmpty(), { it.x }, { it.y })
    val fromRightOffset = groupAndSort(groupedByDirection[-1 to 0].orEmpty(), { it.x }, { it.y })

    var consecutiveDirectionOffsetCounter = 0

    consecutiveDirectionOffsetCounter = processGroupedItems(fromDownOffset, { it.x }, consecutiveDirectionOffsetCounter)
    consecutiveDirectionOffsetCounter = processGroupedItems(fromUpOffset, { it.x }, consecutiveDirectionOffsetCounter)
    consecutiveDirectionOffsetCounter = processGroupedItems(fromLeftOffset, { it.y }, consecutiveDirectionOffsetCounter)
    consecutiveDirectionOffsetCounter = processGroupedItems(fromRightOffset, { it.y }, consecutiveDirectionOffsetCounter)

    return consecutiveDirectionOffsetCounter
}

fun processGroupedItems(groupedItems: Map<Any, List<FreeSideData>>, valueSelector: (FreeSideData) -> Int, counter: Int): Int {
    var updatedCounter = counter
    for (list in groupedItems.values) {
        if (list.size == 1) {
            updatedCounter++
            continue
        }
        var previousValue = -1000
        for (item in list) {
            val currentValue = valueSelector(item)
            if (!isDeltaOne(currentValue, previousValue)) {
                updatedCounter++
            }
            previousValue = currentValue
        }
    }
    return updatedCounter
}

fun groupAndSort(items: List<FreeSideData>, groupBySelector: (FreeSideData) -> Any, sortBySelector: (FreeSideData) -> Int): Map<Any, List<FreeSideData>> {
    return items
        .groupBy(groupBySelector)
        .mapValues { entry -> entry.value.sortedBy(sortBySelector) }
}

private fun isDeltaOne(a: Int, b: Int): Boolean {
    return Math.abs(a - b) == 1
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