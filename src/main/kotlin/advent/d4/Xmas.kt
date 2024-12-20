package advent.d4

import java.io.File

private val REGEX = Regex("(?=(XMAS))")

fun main() {
//    val lines = File("src/main/kotlin/advent/d4/t2.txt").readLines()
    val lines = File("src/main/kotlin/advent/d4/in1.txt").readLines()

    val sum = countForwards(lines) +
              countBackwards(lines) +
              countForwards(rotate90(lines)) +
              countBackwards(rotate90(lines)) +
              countForwards(rotate45(lines)) +
              countBackwards(rotate45(lines)) +
              countForwards(rotate45Counterclockwise(lines)) +
              countBackwards(rotate45Counterclockwise(lines))

    println("F: ${countForwards(lines)}")
    println("B: ${countBackwards(lines)}")
    println("D: ${countForwards(rotate90(lines))}")
    println("U: ${countBackwards(rotate90(lines))}")
    println("DD: ${countForwards(rotate45(lines))}")
    println("DU: ${countBackwards(rotate45(lines))}")
    println("-DD: ${countForwards(rotate45Counterclockwise(lines))}")
    println("-DU: ${countBackwards(rotate45Counterclockwise(lines))}")



    for (line in lines) {
        println(line)
    }
    println()
    for (line in rotate45(lines)) {
        println(line)
    }
    println()
    for (line in rotate45Counterclockwise(lines)) {
        println(line)
    }
    println()
    for (line in rotate90(lines)) {
        println(line)
    }
    println()
    println(sum)
}

private fun countForwards(lines: List<String>): Int {
    return lines.sumOf { line -> REGEX.findAll(line).count() }
}


private fun countBackwards(lines: List<String>): Int =
    lines.sumOf { line -> REGEX.findAll(line.reversed()).count() }

private fun rotate90(lines: List<String>): List<String> {
    return lines.mapIndexed { i, line ->
        line.mapIndexed { j, _ -> lines[j][i] }.joinToString("")
    }
}

private fun rotate45(lines: List<String>): List<String> {
    val numRows = lines.size
    val numCols = numRows
    val groups = mutableMapOf<Int, MutableList<Char>>()

    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            val sum = i + j
            groups.computeIfAbsent(sum) { mutableListOf() }.add(lines[i][j])
        }
    }

    val maxGroupSize = groups.values.maxOf { it.size }
    val totalGroups = groups.keys.maxOrNull() ?: 0

    val rotatedLines = mutableListOf<String>()

    for (row in 0..totalGroups) {
        val group = groups[row] ?: mutableListOf()
        val paddingBefore = maxGroupSize - group.size
        val paddingAfter = row

        val rowChars = CharArray(maxGroupSize + paddingAfter) { '_' }

        for (k in group.indices) {
            rowChars[paddingBefore + k] = group[k]
        }

        rotatedLines.add(String(rowChars))
    }

    return rotatedLines
}

private fun rotate45Counterclockwise(lines: List<String>): List<String> {
    val numRows = lines.size
    val numCols = numRows
    val groups = mutableMapOf<Int, MutableList<Char>>()

    // Collect elements into groups based on the difference of their indices (j - i)
    for (i in 0 until numRows) {
        for (j in 0 until numCols) {
            val diff = j - i
            groups.computeIfAbsent(diff) { mutableListOf() }.add(lines[i][j])
        }
    }

    // Determine the minimum and maximum keys for group iteration
    val minGroupKey = groups.keys.minOrNull() ?: 0
    val maxGroupKey = groups.keys.maxOrNull() ?: 0

    // Build the rotated matrix
    val rotatedLines = mutableListOf<String>()

    for (key in minGroupKey..maxGroupKey) {
        val group = groups[key] ?: mutableListOf()
        val paddingBefore = key - minGroupKey
        val paddingAfter = maxGroupKey - key

        val rowLength = paddingBefore + group.size + paddingAfter
        val rowChars = CharArray(rowLength) { '_' }

        // Fill in the group elements
        for (k in group.indices) {
            rowChars[paddingBefore + k] = group[k]
        }

        rotatedLines.add(String(rowChars))
    }

    return rotatedLines
}




//private fun getDiagUps(lines: List<String>): String {
//    return line
//}
//
//private fun getDiagDowns(lines: List<String>): String {
//    return line.reversed()
//}