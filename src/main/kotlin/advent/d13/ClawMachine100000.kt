package advent.d13

import java.io.File
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.MathContext
import java.math.RoundingMode

data class LargeEqInput(
    val x1: BigDecimal, val y1: BigDecimal, val z1: BigDecimal,
    val x2: BigDecimal, val y2: BigDecimal, val z2: BigDecimal,
)

fun main() {
    val lines = File("src/main/kotlin/advent/d13/i1.txt").readLines()

    val eqInputs = parseLargeEquations(lines)

    var tokenCounter = ZERO
    for (eqInput in eqInputs) {
        val (a, b) = solveLargeEquations(
            eqInput.x1, eqInput.y1, eqInput.z1,
            eqInput.x2, eqInput.y2, eqInput.z2,
        ) ?: Pair(ZERO, ZERO)
        if (isBdInt(a) && isBdInt(b)) {
            tokenCounter += (a * BigDecimal(3)) + b
        }
    }
    println("Tokens: $tokenCounter")
}

private fun isBdInt(value: BigDecimal) =
    value.remainder(BigDecimal.ONE).compareTo(ZERO) == 0

private val XY_PLUS_NUM = Regex("""X\+(\d+),\s*Y\+(\d+)""")
private val XY_EQ_NUM = Regex("""X=(\d+),\s*Y=(\d+)""")
private fun parseLargeEquations(lines: List<String>): List<LargeEqInput> {
    val result = mutableListOf<LargeEqInput>()
    var counter = 1
    var x1 = ZERO
    var x2 = ZERO
    var y1 = ZERO
    var y2 = ZERO
    var z1: BigDecimal
    var z2: BigDecimal
    for (line in lines) {
        if (counter == 1) {
            val matchResult = XY_PLUS_NUM.find(line)
            if (matchResult != null) {
                x1 = BigDecimal(matchResult.groupValues[1])
                x2 = BigDecimal(matchResult.groupValues[2])
            }
        } else if (counter == 2) {
            val matchResult = XY_PLUS_NUM.find(line)
            if (matchResult != null) {
                y1 = BigDecimal(matchResult.groupValues[1])
                y2 = BigDecimal(matchResult.groupValues[2])
            }
        } else if (counter == 3) {
            val matchResult = XY_EQ_NUM.find(line)
            if (matchResult != null) {
                z1 = BigDecimal(matchResult.groupValues[1]).add(BigDecimal("10000000000000"))
                z2 = BigDecimal(matchResult.groupValues[2]).add(BigDecimal("10000000000000"))
                result.add(LargeEqInput(x1, y1, z1, x2, y2, z2))
            }

        }
        if (line.isEmpty()) counter = 1 else counter++
    }
    return result
}

private fun solveLargeEquations(
    x1: BigDecimal, y1: BigDecimal, z1: BigDecimal,
    x2: BigDecimal, y2: BigDecimal, z2: BigDecimal,
): Pair<BigDecimal, BigDecimal>? {
    val mc = MathContext(50, RoundingMode.HALF_UP)

    val determinant = x1.multiply(y2, mc).subtract(x2.multiply(y1, mc), mc)

    if (determinant.compareTo(BigDecimal.ZERO) == 0) return null

    val aNumerator = y2.multiply(z1, mc).subtract(y1.multiply(z2, mc), mc)
    val bNumerator = x1.multiply(z2, mc).subtract(x2.multiply(z1, mc), mc)

    val a = aNumerator.divide(determinant, mc)
    val b = bNumerator.divide(determinant, mc)
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