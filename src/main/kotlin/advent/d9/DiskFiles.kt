package advent.d9

import java.io.File
import java.lang.Character.getNumericValue

class MyFile(val id: Int, val start: Int, var size: Int) {
    override fun toString(): String {
        return "F(id=$id, start=$start, size=$size)"
    }
}

class MySpace(val start: Int, var size: Int) {
    override fun toString(): String {
        return "S(start=$start, size=$size)"
    }
}

fun main() {
    val inStr = File("src/main/kotlin/advent/d9/i1.txt").readLines()[0]

    val decoded = decode(inStr)
    println("Decoded: ${decoded.map { if (it == -1) "_" else it }.joinToString()}")

    val compressedDisk = compress(decoded)
    println("Compressed: ${compressedDisk.map { if (it == -1) "_" else it }.joinToString()}")

    val checksum = checkSum(compressedDisk)
    println("Checksum: $checksum")
}

private fun compress(decoded: List<Int>): List<Int> {
    var trimmedDisk = trimDisk(decoded)
    val files = parseFiles(trimmedDisk)
    val spaces = parseSpaces(trimmedDisk)

    var currentFiles = files
    var currentSpaces = spaces
    for (file in files.reversed()) {
        println("CurrentFile: $file")
        val firstAccSpace = currentSpaces.find { it.size >= file.size && it.start < file.start }
        if (firstAccSpace != null) {
            trimmedDisk = moveToFront(currentFiles.find { it.id == file.id }!!, firstAccSpace, trimmedDisk)
        } else {
            currentFiles = currentFiles.dropLast(1)
            continue
        }
        println("Before parseFiles")
        currentFiles = parseFiles(trimmedDisk)
        println("Before parseSpaces")
        currentSpaces = parseSpaces(trimmedDisk)
    }
    return trimmedDisk
}

private fun moveToFront(file: MyFile, firstSpace: MySpace, disk: List<Int>): List<Int> {
    val mutableList = disk.toMutableList()
    var counter = 0
    for (i in firstSpace.start until (firstSpace.start + file.size)) {
        mutableList[i] = file.id
        mutableList[file.start + counter++] = FREE
//        println("MOVED: " + mutableList.map { if (it == -1L) "_" else it }.joinToString())
    }
    return mutableList
}

private fun parseSpaces(list: List<Int>): List<MySpace> {
    val spaces = mutableListOf<MySpace>()
    var startIdx = 0
    var spaceSizeCounter = 0
    for ((i, el) in list.withIndex()) {
        if ((i >= 1) && (list[i - 1] != FREE) && (el == FREE)) {
            startIdx = i
        }
        if (el == FREE && ((i+1) >= list.size || list[i + 1] != FREE)) {
            spaces.add(MySpace(startIdx, spaceSizeCounter + 1))
            spaceSizeCounter = 0
        } else if (el == FREE) {
            spaceSizeCounter++
        }
    }
    return spaces
}

//private fun parseFiles(list: List<Int>): List<MyFile> {
//    val files = mutableListOf<MyFile>()
//    var currentFileId = -1
//    for ((idx, el) in list.withIndex()) {
//        if (currentFileId == el) continue
//        if (currentFileId != el && el != FREE) {
//            files.add(MyFile(el, idx, list.count { it == el}))
//            currentFileId = el
//        }
//        if (el == FREE) {
//            currentFileId = el
//        }
//    }
//    return files
//}

private fun parseFiles(list: List<Int>): List<MyFile> {
    // Precompute the frequency of each file ID (excluding FREE)
    val countMap = mutableMapOf<Int, Int>()
    for (el in list) {
        if (el != FREE) {
            countMap[el] = countMap.getOrDefault(el, 0) + 1
        }
    }
    val files = mutableListOf<MyFile>()
    var currentFileId = -1
    for (i in list.indices) {
        val el = list[i]
        // If we encounter a different file ID than the current one, and it's not FREE
        if (el != FREE && el != currentFileId) {
            // Add a new MyFile with the precomputed count
            files.add(MyFile(el, i, countMap[el] ?: 0))
            currentFileId = el
        } else if (el == FREE) {
            // Reset the current file to FREE so the next file ID will be recorded
            currentFileId = FREE
        }
    }
    return files
}


private fun trimDisk(list: List<Int>) = list.dropLastWhile { it == FREE }

private const val FREE = -1

private fun decode(inStr: String): List<Int> {
    val result = mutableListOf<Int>()
    var isNumToggle = true
    var id = 0
    for (i in 0 until inStr.length) {
        val intgr = getNumericValue(inStr[i])
        for (j in 0 until intgr) {
            if (isNumToggle) {
                result.add(id)
            } else {
                result.add(-1)
            }
        }
        if (isNumToggle) id++
        isNumToggle = !isNumToggle
    }
    return result
}

private fun checkSum(compressedDisk: List<Int>): Long {
    return compressedDisk
        .mapIndexed { idx, num -> idx.toLong() * (if (num == -1) 0 else num) }
        .sumOf { it }
}