package advent.d11

import java.io.File
import java.util.*

fun main() {
    val inStr = File("src/main/kotlin/advent/d11/i1.txt").readLines()[0]

    val nonApplied = HashMap<String, Long>()

    inStr.split(" ").forEach { nonApplied[it] = (nonApplied[it] ?: 0L) + 1L }

    for (i in 1..75) {
        val sizeBefore = totalCount(nonApplied)
        val nextNonApplied = runIteration(nonApplied)
        val sizeAfter = totalCount(nextNonApplied)
        println("Size after $i blink: $sizeAfter (was $sizeBefore before)")
        nonApplied.clear()
        nonApplied.putAll(nextNonApplied)
    }
}

private fun runIteration(nonApplied: Map<String, Long>): Map<String, Long> {
    val applied = HashMap<String, Long>()
    // 1. replace0to1
    replace0to1(nonApplied, applied)
    val afterReplace = mergeMapsExcludeKeys(nonApplied, applied, listOf("0"))
    // 2. splitEvenDigitNumbers
    val afterSplit = splitEvenDigitNumbers(afterReplace.first, afterReplace.second)
    // 3. multiplyBy2024
    val afterMultiply = multiplyBy2024(afterSplit.first, afterSplit.second)
    // Reset applied: merge applied & nonApplied
    return mergeMapsAndReset(afterMultiply.first, afterMultiply.second)
}

// 1ST
private fun replace0to1(nonApplMap: Map<String, Long>, applMap: MutableMap<String, Long>) {
    val zeroCount = nonApplMap["0"]
    if (zeroCount != null) applMap["1"] = (applMap["1"] ?: 0L) + zeroCount
}

private fun splitEvenDigitNumbers(
    nonApplied: Map<String, Long>,
    applied: Map<String, Long>
): Pair<Map<String, Long>, Map<String, Long>> {
    val newNonApplied = HashMap<String, Long>(nonApplied.size)
    val newApplied = HashMap<String, Long>(applied.size)

    // Copy already applied items
    for ((value, count) in applied) {
        newApplied[value] = (newApplied[value] ?: 0L) + count
    }

    for ((value, count) in nonApplied) {
        if (value.length % 2 == 0 && value.isNotEmpty()) {
            val half = value.length / 2
            val left = removeLeadingZeros(value.substring(0, half))
            val right = removeLeadingZeros(value.substring(half))

            newApplied[left] = (newApplied[left] ?: 0L) + count
            newApplied[right] = (newApplied[right] ?: 0L) + count
        } else {
            newNonApplied[value] = (newNonApplied[value] ?: 0L) + count
        }
    }

    return Pair(newNonApplied, newApplied)
}

private fun multiplyBy2024(
    nonApplied: Map<String, Long>,
    applied: Map<String, Long>
): Pair<Map<String, Long>, Map<String, Long>> {
    val newNonApplied = HashMap<String, Long>(nonApplied.size)
    val newApplied = HashMap<String, Long>(applied.size)
    // Copy already applied
    for ((value, count) in applied) {
        newApplied[value] = (newApplied[value] ?: 0L) + count
    }
    val factor = 2024L
    for ((value, count) in nonApplied) {
        val num = value.toLong()
        val mul = (num * factor).toString()
        newApplied[mul] = (newApplied[mul] ?: 0L) + count
    }
    return Pair(newNonApplied, newApplied)
}

private fun mergeMapsAndReset(
    nonApplied: Map<String, Long>,
    applied: Map<String, Long>
): Map<String, Long> {
    val result = HashMap<String, Long>(nonApplied.size + applied.size)
    for ((value, count) in nonApplied) {
        result[value] = (result[value] ?: 0L) + count
    }
    for ((value, count) in applied) {
        result[value] = (result[value] ?: 0L) + count
    }
    return result
}

private fun mergeMapsExcludeKeys(
    nonApplied: Map<String, Long>,
    applied: Map<String, Long>,
    excludeKeys: List<String>
): Pair<Map<String, Long>, Map<String, Long>> {
    val newNonApplied = HashMap<String, Long>(nonApplied.size)
    val newApplied = HashMap<String, Long>(applied.size)

    // Add applied items
    for ((value, count) in applied) {
        newApplied[value] = (newApplied[value] ?: 0L) + count
    }

    // Copy nonApplied except excluded keys
    for ((value, count) in nonApplied) {
        if (value !in excludeKeys) {
            newNonApplied[value] = (newNonApplied[value] ?: 0L) + count
        }
    }
    return Pair(newNonApplied, newApplied)
}

private fun totalCount(map: Map<String, Long>): Long {
    var sum = 0L
    for (count in map.values) sum += count
    return sum
}

private fun removeLeadingZeros(str: String): String {
    var idx = 0
    val len = str.length
    while (idx < len && str[idx] == '0') idx++
    return if (idx == len) "0" else str.substring(idx)
}