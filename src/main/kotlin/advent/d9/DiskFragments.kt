package advent.d9

import java.io.File
import java.lang.Character.getNumericValue

fun main() {
    val inStr = File("src/main/kotlin/advent/d9/i1.txt").readLines()[0]

    val decoded = decode(inStr)
//    println("Decoded: ${decoded}")

    val compressedDisk = compress(decoded)
    println(compressedDisk)

    val checksum = checkSum(compressedDisk)
    println("Checksum: $checksum")
}

private fun compress(decoded: List<Short>): List<Short> {
    var trimmedDisk = trimDisk(decoded)
    var freeIdx = firstFeeIdx(trimmedDisk)
    while (freeIdx != -1) {
        trimmedDisk = moveLastToFront(freeIdx, trimmedDisk)
        trimmedDisk = trimDisk(trimmedDisk)
        freeIdx = firstFeeIdx(trimmedDisk)
    }
    return trimmedDisk
}

private fun moveLastToFront(firstFreeIdx: Int, list: List<Short>): List<Short> {
    var mutableList = list.toMutableList()
    val lastIdx = mutableList.lastIndex
    val lastEl = mutableList.last()
    mutableList[firstFreeIdx] = lastEl
    mutableList[lastIdx] = FREE
    return mutableList
}

private fun trimDisk(list: List<Short>): List<Short> {
    return list.dropLastWhile { it == FREE }
}

private const val FREE = (-1).toShort()
private fun firstFeeIdx(list: List<Short>) = list.indexOfFirst { it == FREE }

private fun decode(inStr: String): List<Short> {
    val result = mutableListOf<Short>()
    var isNumToggle = true
    var id = 0
    for (i in 0 until inStr.length) {
        val intgr = getNumericValue(inStr[i])
        for (j in 0 until intgr) {
            if (isNumToggle) {
                result.add(id.toShort())
            } else {
                result.add(-1)
            }
        }
        if (isNumToggle) {
            id++
        }
        isNumToggle = !isNumToggle
    }
    return result
}

private fun checkSum(compressedDisk: List<Short>): Long {
    return compressedDisk.mapIndexed { idx, sh -> idx.toLong() * sh }.sumOf { it }
}