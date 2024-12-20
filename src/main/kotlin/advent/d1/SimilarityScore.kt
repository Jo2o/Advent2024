package advent.d1

import java.io.File

fun main() {
    val file = File("src/main/kotlin/advent/d1/in1.txt")

    val list1 = mutableListOf<Int>()
    val map2 = mutableMapOf<Int, Int>()

    file.bufferedReader().use { reader -> reader.forEachLine { line -> run {
        val (n1, n2) = getNumbers(line)
        addOrdered(n1, list1)
        addMap(n2, map2)
    }}}

    var sum = 0

    list1.forEach { n -> run {
        sum += n * (map2[n] ?: 0)
    }}

    print(sum)
}

private fun getNumbers(line: String): Pair<Int, Int> {
    val nums = line.split("\\s+".toRegex()).map { it.toInt() }
    return Pair(nums[0], nums[1])
}

private fun addOrdered(n: Int, list: MutableList<Int>) {
    val i = list.binarySearch(n).let { if (it < 0) -it - 1 else it }
    list.add(i, n)
}

private fun addMap(n: Int, map: MutableMap<Int, Int>) {
    map[n] = (map[n] ?: 0) + 1
}