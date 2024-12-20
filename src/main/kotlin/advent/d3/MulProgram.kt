package advent.d3

import java.io.File

fun main() {
    val file = File("src/main/kotlin/advent/d3/in1.txt")

    val mulPattern = Regex("""mul\(\d{1,3},\d{1,3}\)""")
    val twoNumbersPattern = Regex("""\d{1,3},\d{1,3}""")

    var sum = 0
    file.bufferedReader().use { reader -> reader.forEachLine { line -> run {
        val muls = mulPattern.findAll(line).map { it.value }.toList()
        for (mul in muls) {
            sum += twoNumbersPattern.findAll(mul)
                .map { it.value.split(",").map { it.toInt() } }
                .toList()
                .sumOf { it[0] * it[1] }
        }
    }}}

    print(sum)
}