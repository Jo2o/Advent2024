package advent.d7

import java.io.File

fun main() {
    val inStr = File("src/main/kotlin/advent/d7/in1.txt").readLines()
//    val inStr = File("src/main/kotlin/advent/d7/in1.txt").readLines()

    var total = 0L

    for (line in inStr) {
        val parts = line.split(":")
        if (parts.size != 2) continue // Skip malformed lines

        val possibleResult = parts[0].toLongOrNull() ?: continue // Skip if not a valid number
        val numStrs = parts[1]
            .split(" ")
            .filter { it.isNotBlank() }
            .joinToString("") { it }

        val numNums = createNumNums(numStrs)

        for (nums in numNums) {
            var levels = mutableSetOf(nums[0])
            for (i in 1 until nums.size) {
                val currentNum = nums[i]
                val newLevels = mutableSetOf<Long>()
                for (l in levels) {
                    newLevels.add(currentNum + l)
                    newLevels.add(currentNum * l)
                }
                levels = newLevels
            }
            if (possibleResult in levels) {
                total += possibleResult
                break // Avoid counting the same `possibleResult` multiple times
            }
        }
    }
    println("Total: $total")
}

private fun createNumNums(numStrs: String): List<List<Long>> {
    val result = mutableListOf<List<Long>>()
    fun backtrack(start: Int, currentPartition: MutableList<Long>) {
        if (start == numStrs.length) {
            result.add(currentPartition.toList())
            return
        }
        for (i in start + 1..numStrs.length) {
            val substring = numStrs.substring(start, i)
            currentPartition.add(substring.toLong())
            backtrack(i, currentPartition)
            currentPartition.removeAt(currentPartition.size - 1)
        }
    }
    backtrack(0, mutableListOf())
    return result
}