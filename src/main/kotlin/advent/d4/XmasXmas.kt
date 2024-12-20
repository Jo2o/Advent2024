package advent.d4

import java.io.File

fun main() {
    val lines = File("src/main/kotlin/advent/d4/in1.txt").readLines()

    var sum = 0
    for (i in 1..(lines.size - 2)) {
        val lineBefore = lines[i - 1]
        val currentLine = lines[i]
        val lineAfter = lines[i + 1]
        for (j in 1..(currentLine.length - 2)) {
            val currentChar = currentLine[j]
            if (currentChar == 'A') {
                var mCount = 0
                if (lineBefore[j - 1] == 'M') mCount++
                if (lineBefore[j + 1] == 'M') mCount++
                if (lineAfter[j - 1] == 'M') mCount++
                if (lineAfter[j + 1] == 'M') mCount++

                var sCount = 0
                if (lineBefore[j - 1] == 'S') sCount++
                if (lineBefore[j + 1] == 'S') sCount++
                if (lineAfter[j - 1] == 'S') sCount++
                if (lineAfter[j + 1] == 'S') sCount++

                if ((mCount == 2 && sCount == 2) && (lineBefore[j - 1] != lineAfter[j + 1])) {
                    println("Found X-MAS at [$i, $j]")
                    sum++
                }
            }
        }
    }
    println(sum)
}