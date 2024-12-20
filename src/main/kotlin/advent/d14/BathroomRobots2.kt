package advent.d14

import java.io.File

private const val WIDTH = 101
private const val HEIGHT = 103

fun main() {
    val lines = File("src/main/kotlin/advent/d14/i1.txt").readLines()
    val robotsData = parseRobots(lines)

    for (step in 1..15000) {
        val roboPositions = step(robotsData, step)
        val res = plot(roboPositions)
        println("Step: $step")
        if (res) {
            break
        }
//        Thread.sleep(300)
    }
}

private fun plot(roboPositions: List<RobotPosition>): Boolean {
    val grid = Array(HEIGHT) { CharArray(WIDTH) { ' ' } }
    for (roboPos in roboPositions) {
        grid[roboPos.y][roboPos.x] = '#'
    }
    var count = false
    for (row in grid) {
        println(row)   //(row.joinToString("").replace(" ", "  ").replace("#", "##"))
        if (row.joinToString("").contains("##########")) {
            count = true
        }
    }
    return count
}


private fun step(robotsData: List<RobotData>, step: Int): List<RobotPosition> {
    val roboPositions = mutableListOf<RobotPosition>()
    for (roboData in robotsData) {
        val tmpX = (roboData.x + roboData.dx * step) % WIDTH
        var x: Int
        if (tmpX < 0) {
            x = WIDTH + tmpX
        } else {
            x = tmpX
        }
        val tmpY = (roboData.y + roboData.dy * step) % HEIGHT
        var y: Int
        if (tmpY < 0) {
            y = HEIGHT + tmpY
        } else {
            y = tmpY
        }
        roboPositions.add(RobotPosition(x, y))
    }
    return roboPositions
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