package advent.d17

import java.io.File

fun main() {
    val inStrs = File("src/main/kotlin/advent/d17/i1.txt").readLines()

    val regs = mutableListOf(
        parseRegister(inStrs[0]),
        parseRegister(inStrs[1]),
        parseRegister(inStrs[2]),
    )

    val program = parseProgram(inStrs[4])

    val output = mutableListOf<String>()
    var i = 0
    while (i < program.size) {
        when (program[i]) {
            0 -> regs[0] = adv0(regs, program[i + 1])
            1 -> regs[1] = bxl1(regs, program[i + 1])
            2 -> regs[1] = bst2(regs, program[i + 1])
            3 -> {
                val jump = jnz3(regs, program[i + 1])
                if (jump != -1) {
                    i = jump
                    i -= 2
                }
            }
            4 -> regs[1] = bxc4(regs, program[i + 1])
            5 -> output.add(out5(regs, program[i + 1]))
            6 -> regs[1] = bdv6(regs, program[i + 1])
            7 -> regs[2] = cdv7(regs, program[i + 1])
        }
        i += 2
    }
    println(output.joinToString(","))
    println("Registry: $regs")
}

private fun adv0(regs: List<Int>, op: Int): Int {
    return regs[0] / (1 shl combo(op, regs))
}

private fun bxl1(regs: List<Int>, op: Int): Int {
    return regs[1] xor op
}

private fun bst2(regs: List<Int>, op: Int): Int {
    return combo(op, regs) % 8
}

private fun jnz3(regs: List<Int>, op: Int): Int {
    if (regs[0] == 0) return -1 // do nothing
    return op // jump!!!
}

private fun bxc4(regs: List<Int>, op: Int): Int {
    return regs[1] xor regs[2]
}

private fun out5(regs: List<Int>, op: Int): String {
    return (combo(op, regs) % 8).toString()
}

private fun bdv6(regs: List<Int>, op: Int): Int {
    return adv0(regs, op)
}

private fun cdv7(regs: List<Int>, op: Int): Int {
    return adv0(regs, op)
}

private fun combo(op: Int, regs: List<Int>): Int = when {
    op in 0..3 -> op
    op == 4 -> regs[0]
    op == 5 -> regs[1]
    op == 6 -> regs[2]
    else -> throw IllegalArgumentException("Invalid operand: $op")
}

private fun parseRegister(str: String): Int {
    return str.substringAfter(":").trim().toInt()
}

private fun parseProgram(str: String): List<Int> {
    return str.substringAfter(":").split(",").map { it.trim().toInt() }
}