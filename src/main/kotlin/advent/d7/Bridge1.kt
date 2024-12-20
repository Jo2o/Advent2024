package advent.d7

import java.io.File

fun main() {
//    val inStr = File("src/main/kotlin/advent/d7/in1.txt").readLines()
    val inStr = File("src/main/kotlin/advent/d7/t2.txt").readLines()

    var total = 0L

    for (line in inStr) {
        val possibleResult = line.split(":")[0].toLong()
        val nums = line.split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        var levels = mutableListOf(nums[0])
        for (i in 1 until nums.size) {
            val currentNum = nums[i]
            print("$currentNum -> ")
            val newLevels = mutableListOf<Long>()
            for (l in levels) {
                newLevels.add(currentNum + l)
                newLevels.add(currentNum * l)
            }
            levels = newLevels
            println(levels)
        }
        println(possibleResult)
        if (levels.contains(possibleResult)) {
            total += possibleResult
        }
        println("Total: $total")
    }
}