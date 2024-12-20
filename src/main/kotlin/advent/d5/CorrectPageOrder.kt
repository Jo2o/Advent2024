package advent.d5

import java.io.File

fun main() {
//    val ruleLines = File("src/main/kotlin/advent/d5/t11.txt").readLines()
//    val updateLines = File("src/main/kotlin/advent/d5/t12.txt").readLines()
    val ruleLines = File("src/main/kotlin/advent/d5/in11.txt").readLines()
    val updateLines = File("src/main/kotlin/advent/d5/in12.txt").readLines()

    val rulePairs = ruleLines.map {
        val (l, r) = it.split("|").map { it.toInt() }
        Pair(l, r)
    }

    var total = 0
    val faultyUpdates = mutableListOf<List<Int>>()

    updateLines.forEach { updateLine -> run {
        val updateList = updateLine.split(",").map { it.toInt() }

        var allRulesFound = true
        for (i in updateList.indices) {
            val updNo = updateList[i]
            for (j in (i + 1)..< updateList.size) {

                val targetPair = Pair(updNo, updateList[j])
                if (!rulePairs.contains(targetPair)) {
                    faultyUpdates.add(updateList)
                    allRulesFound = false
                    break
                }
            }
            if (!allRulesFound) break
        }
        if (allRulesFound) {
            total += getMiddle(updateList)
        }
    }}
    println("Good total: $total")

    val totalFixed = faultyUpdates.sumOf { getMiddle(fixUpdate(it, rulePairs)) }

    println("Fixed total: $totalFixed")
}

private fun fixUpdate(list: List<Int>, rulePairs: List<Pair<Int, Int>>): List<Int> {
    val pairCounter = mutableMapOf<Int, Int>()
    for (i in list.indices) {
        val no = list[i]
        pairCounter[no] = 0
        for (j in list.indices) {
            if (i == j) continue
            val targetPair = Pair(no, list[j])
            if (rulePairs.contains(targetPair)) {
                pairCounter[no] = pairCounter[no]!! + 1
                continue
            }
        }
    }

    return pairCounter.entries.sortedByDescending { it.value }.map { it.key }
}

private fun getMiddle(list: List<Int>): Int {
    return list[list.size / 2]
}