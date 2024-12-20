package advent.d3

import java.io.File

fun main() {
//    val file = File("src/main/kotlin/advent/d3/inoneline.txt")
    val file = File("src/main/kotlin/advent/d3/in1.txt")
//    val file = File("src/main/kotlin/advent/d3/intest.txt")

    val mulPattern = Regex("""mul\(\d{1,3},\d{1,3}\)""")
    val doPattern = Regex("""do\(\)""")
    val dontPattern = Regex("""don't\(\)""")
    val twoNumbersPattern = Regex("""\d{1,3},\d{1,3}""")

    var sum = 0
    var overLineDoSwitch = true

    file.bufferedReader().use { reader -> reader.forEachLine { line -> run {
        val dos = doPattern.findAll(line)
        val donts = dontPattern.findAll(line)
        val muls = mulPattern.findAll(line)

        val doStarts = mutableListOf<Int>()
        for (do_ in dos) { doStarts.add(do_.range.first) }

        val dontStarts = mutableListOf<Int>()
        for (dont in donts) { dontStarts.add(dont.range.first) }

        if (overLineDoSwitch) {
            doStarts.add(0, 0)
        } else {
            dontStarts.add(0, 0)
        }

        val mulStarts = mutableListOf<Int>()
        for (mul in muls) { mulStarts.add(mul.range.first) }

        for (mul in muls) {
            val mulStart = mul.range.first
            if (getCurrentDoStart(mulStart, doStarts) > getCurrentDontStart(mulStart, dontStarts)) {
                sum += twoNumbersPattern.findAll(mul.value)
                    .map { it.value.split(",").map { it.toInt() } }
                    .toList()
                    .sumOf { it[0] * it[1] }
            }
        }
        overLineDoSwitch = getOverLineDoSwitch(doStarts.last(), dontStarts.last())
    }}}

    print(sum)
}

private fun getCurrentDoStart(mulStart: Int, doStarts: List<Int>): Int {
    return doStarts.lastOrNull { it <= mulStart } ?: Int.MIN_VALUE
}

private fun getCurrentDontStart(mulStart: Int, dontStarts: List<Int>): Int {
    return dontStarts.lastOrNull { it <= mulStart } ?: Int.MIN_VALUE
}

private fun getOverLineDoSwitch(lastDoStart: Int, lastDontStart: Int): Boolean {
    return lastDoStart > lastDontStart
}