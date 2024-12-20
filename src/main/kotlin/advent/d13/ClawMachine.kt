package advent.d13

import java.io.File

data class EqInput(
    val x1: Int, val y1: Int, val z1: Long,
    val x2: Int, val y2: Int, val z2: Long,
)

fun main() {
    val lines = File("src/main/kotlin/advent/d13/t1.txt").readLines()

    val eqInputs = parseEquations(lines)

    var tokenCounter = 0
    for (eqInput in eqInputs) {
        val (a, b) = solveEquations(
            eqInput.x1.toDouble(), eqInput.y1.toDouble(), eqInput.z1.toDouble(),
            eqInput.x2.toDouble(), eqInput.y2.toDouble(), eqInput.z2.toDouble()
        ) ?: Pair(0.0, 0.0)
        if (isInt(a) && isInt(b)) {
            tokenCounter += 3 * a.toInt() + b.toInt()
        }
    }
    println("Tokens: $tokenCounter")
}

private fun isInt(value: Double) = value % 1.0 == 0.0

private val XY_PLUS_NUM = Regex("""X\+(\d+),\s*Y\+(\d+)""")
private val XY_EQ_NUM = Regex("""X=(\d+),\s*Y=(\d+)""")
private fun parseEquations(lines: List<String>): List<EqInput> {
    val result = mutableListOf<EqInput>()
    var counter = 1
    var x1 = 0
    var x2 = 0
    var y1 = 0
    var y2 = 0
    var z1: Long
    var z2: Long
    for (line in lines) {
        if (counter == 1) {
            val matchResult = XY_PLUS_NUM.find(line)
            if (matchResult != null) {
                x1 = matchResult.groupValues[1].toInt()
                x2 = matchResult.groupValues[2].toInt()
            }
        } else if (counter == 2) {
            val matchResult = XY_PLUS_NUM.find(line)
            if (matchResult != null) {
                y1 = matchResult.groupValues[1].toInt()
                y2 = matchResult.groupValues[2].toInt()
            }
        } else if (counter == 3) {
            val matchResult = XY_EQ_NUM.find(line)
            if (matchResult != null) {
                z1 = matchResult.groupValues[1].toInt() + 10000000000000
                z2 = matchResult.groupValues[2].toInt() + 10000000000000
                result.add(EqInput(x1, y1, z1, x2, y2, z2))
            }

        }
        if (line.isEmpty()) counter = 1 else counter++
    }
    return result
}

private fun solveEquations(
    x1: Double, y1: Double, z1: Double,
    x2: Double, y2: Double, z2: Double
): Pair<Double, Double>? {
    val determinant = x1 * y2 - x2 * y1
    if (determinant == 0.0) return null
    val a = (y2 * z1 - y1 * z2) / determinant
    val b = (x1 * z2 - x2 * z1) / determinant
    return Pair(a, b)
}

//println(
//solveEquations(
//94.0, 22.0, 8400.0,
//34.0, 67.0, 5400.0,
//)
//)
//println(
//solveEquations(
//26.0, 67.0, 12748.0,
//66.0, 21.0, 12176.0,
//)
//)
//println(
//solveEquations(
//17.0, 84.0, 7870.0,
//86.0, 37.0, 6450.0,
//)
//)