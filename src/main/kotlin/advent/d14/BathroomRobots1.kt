package advent.d14

import java.io.File

private const val WIDTH = 101
private const val HEIGHT = 103
//private const val WIDTH = 3
//private const val HEIGHT = 3
//private const val TWIDTH = 11
//private const val THEIGHT = 7
private const val SECONDS = 100

data class RobotData(
    val id: String,
    val x: Int, val y: Int,
    val dx: Int, val dy: Int,
)

data class RobotPosition(
    val x: Int, val y: Int
)

fun main() {
    val lines = File("src/main/kotlin/advent/d14/i1.txt").readLines()
    val robotsData = parseRobots(lines)

    val roboPositions = mutableListOf<RobotPosition>()
    for (roboData in robotsData) {
        val tmpX = (roboData.x + roboData.dx * SECONDS) % WIDTH
        var x: Int
        if (tmpX < 0) {
            x = WIDTH + tmpX
        } else {
            x = tmpX
        }
        val tmpY = (roboData.y + roboData.dy * SECONDS) % HEIGHT
        var y: Int
        if (tmpY < 0) {
            y = HEIGHT + tmpY
        } else {
            y = tmpY
        }
        roboPositions.add(RobotPosition(x, y))
    }

    countQuadrants(roboPositions)
}

private fun countQuadrants(roboPositions: List<RobotPosition>) {
    var tlq = 0
    var trq = 0
    var dlq = 0
    var drq = 0
    for (roboPos in roboPositions) {
        if (roboPos.x < WIDTH / 2 && roboPos.y < HEIGHT / 2) {
            tlq++
        } else if (roboPos.x > WIDTH / 2 && roboPos.y < HEIGHT / 2) {
            trq++
        } else if (roboPos.x < WIDTH / 2 && roboPos.y > HEIGHT / 2) {
            dlq++
        } else if (roboPos.x > WIDTH / 2 && roboPos.y > HEIGHT / 2) {
            drq++
        }
    }
    println("Result: ${tlq * trq * dlq * drq}")
}

private val REGEX = Regex("""p=(-?\d+),(-?\d+)\s+v=(-?\d+),(-?\d+)""")
private fun parseRobots(lines: List<String>): List<RobotData> {
    val result = mutableListOf<RobotData>()
    for (line in lines) {
        val matchResult = REGEX.find(line) ?: throw IllegalArgumentException("Input string is not in the expected format.")
        val (x, y, dx, dy) = matchResult.destructured
        result.add(
            RobotData(
                id = x + "-" + y,
                x = x.toInt(),
                y = y.toInt(),
                dx = dx.toInt(),
                dy = dy.toInt()
            )
        )
    }
    return result
}