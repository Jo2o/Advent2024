package advent.d2

import java.io.File

private const val DECR = "DECR"
private const val INCR = "INCR"

fun main() {
    val file = File("src/main/kotlin/advent/d2/in2.txt")

    var safeCount = 0

//    file.bufferedReader().use { reader -> reader.forEachLine { line -> run {
//        if (validateSequence(line)) {
//            safeCount++
//        } else {
//            println("Invalid seq: $line")
//        }
//    }}}

    print(safeCount)
}

//fun validateSequence(line: String, stopper: Boolean = false): Boolean {
//    val numStrs = line.split(" ")
//    var nums = numStrs.map { it.toInt() }.toMutableList()
//    var prev = nums[0]
//
//    if ((nums[1] == nums[0]) || (abs(nums[1] - nums[0]) > 3)) {
//        return if (!stopper) {
//            validateSequence(backToLineWithoutIdx(nums, 0), true)
//        } else {
//            false
//        }
//    }
//
//    var trend = if (nums[1] > nums[0]) INCR else DECR
//
//    for ((i, n) in nums.withIndex()) {
//        if (i == 0) continue
//
//        if (abs(n - prev) > 3 || n == prev) {
//            return if (!stopper) {
//                validateSequence(backToLineWithoutIdx(nums, i), true)
//            } else {
//                false
//            }
//        }
//
//        val trend2 = if (n > prev) INCR else DECR
//
//        prev = n
//
//        if (trend2 != trend ) {
//            return if (!stopper) {
//                validateSequence(backToLineWithoutIdx(nums, i), true)
//            } else {
//                false
//            }
//        } else {
//            continue
//        }
//    }
//    return true
//}

//private fun backToLineWithoutIdx(nums: MutableList<Int>, idx: Int): String {
//    nums.removeAt(idx)
//    return nums.joinToString(" ")
//}