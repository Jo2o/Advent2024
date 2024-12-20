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

    updateLines.forEach { updateLine -> run {
        val updateList = updateLine.split(",").map { it.toInt() }

        var allRulesFound = true
        for (i in 0..< updateList.size) {
            val updNo = updateList[i]
            for (j in (i + 1)..< updateList.size) {
                if (i == j) continue
                val targetPair = Pair(updNo, updateList[j])
                if (!rulePairs.contains(targetPair)) {
                    allRulesFound = false
                    break
                }
            }
        }
        if (allRulesFound) {
            total += getMiddle(updateList)
        }
    }}

    println(total)
}

private fun getMiddle(list: List<Int>): Int {
    return list[list.size / 2]
}