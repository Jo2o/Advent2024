package advent.d2

import java.io.File
import kotlin.math.abs

private const val DECR = "DECR"
private const val INCR = "INCR"

fun main() {
    val file = File("src/main/kotlin/advent/d2/in1.txt")

    var safeCount = 0

    file.bufferedReader().use { reader ->
        reader.forEachLine { line ->
            run {
                val nums = line.split(" ").map { it.toInt() }
                if (validateSequence(nums)) {
                    safeCount++
                } else {
                    for (i in nums.indices) {
                        if (validateSequence(backToLineWithoutIdx(nums.toMutableList(), i))) {
                            safeCount++
                            break
                        }
                    }
                }
            }
        }
    }
    print(safeCount)
}

private fun validateSequence(nums: List<Int>): Boolean {
    if (nums[1] == nums[0]) return false

    var prev = nums[0]

    val trend = if (nums[1] > nums[0]) INCR else DECR

    for ((i, n) in nums.withIndex()) {
        if (i == 0) continue

        if (abs(n - prev) > 3 || n == prev) {
            return false
        }

        val trend2 = if (n > prev) INCR else DECR

        prev = n

        if (trend2 != trend) {
            return false
        } else {
            continue
        }
    }
    return true
}

private fun backToLineWithoutIdx(nums: MutableList<Int>, idx: Int): List<Int> {
    nums.removeAt(idx)
    return nums
}