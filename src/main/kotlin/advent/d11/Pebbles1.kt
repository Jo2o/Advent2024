package advent.d11

import java.io.File

class Item(var value: String, var applied: Boolean) {
    override fun toString(): String {
        return "It(val='$value', appl=$applied)"
    }
}


fun main() {
    val inStr = File("src/main/kotlin/advent/d11/i1.txt").readLines()[0]
//    val inStr = File("src/main/kotlin/advent/d11/t2.txt").readLines()[0]
    var items = inStr
        .split(" ")
        .map { Item(it, false) }
        .toMutableList()

    for (i in 1..25) {
        replace0to1(items)
        splitEvenDigitNumbers(items)
        multiplyBy2024(items)
        resetApplied(items)

//        println(items.map { it.value }.joinToString(" "))
        println("Size after $i blink: ${items.size}")
    }
}

private fun resetApplied(list: MutableList<Item>) {
    for (i in list.indices) {
            list[i] = Item(list[i].value, false)
    }
}

// 1ST
private fun replace0to1(list: MutableList<Item>) {
    for (i in list.indices) {
        if (list[i].value == "0") {
            list[i] = Item("1", true)
        }
    }
}

// 2ND
private fun splitEvenDigitNumbers(list: MutableList<Item>) {
    var i = 0
    while (i < list.size) {
        val item = list[i].value
        val length = item.length
        if (length % 2 == 0 && list[i].applied.not()) {
            val half = length / 2
            val left = item.substring(0, half).toInt().toString()
            val right = item.substring(half).toInt().toString()
            list[i] = Item(left, true)
            list.add(i + 1, Item(right, true))
            i += 2
        } else {
            i++
        }
    }
}

// 3RD
private fun multiplyBy2024(list: MutableList<Item>) {
    for (i in list.indices) {
        if (list[i].applied.not()) {
            list[i] = Item(
                (list[i].value.toLong() * 2024).toString(),
                true
            )
        }
    }
}