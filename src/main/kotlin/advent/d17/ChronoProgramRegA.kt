package advent.d17

import java.io.File

fun main() {
    val inStrs = File("src/main/kotlin/advent/d17/i1.txt").readLines()

    for (j in 0..10000000000) {
        val regs = mutableListOf(
            j,
            parseRegister(inStrs[1]).toLong(),
            parseRegister(inStrs[2]).toLong(),
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
                    if (jump != -1L) {
                        i = jump.toInt()
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
        if (output.joinToString(",") == inStrs[4].substringAfter(":").trim()) {
            println(output.joinToString(","))
            println("REG-A: ${j}")
            break
        }
        println(".")
    }

}

private fun adv0(regs: List<Long>, op: Int): Long {
    return regs[0] / (1 shl combo(op, regs).toInt())
}

private fun bxl1(regs: List<Long>, op: Int): Long {
    return regs[1] xor op.toLong()
}

private fun bst2(regs: List<Long>, op: Int): Long {
    return combo(op, regs) % 8
}

private fun jnz3(regs: List<Long>, op: Int): Long {
    if (regs[0] == 0L) return -1L // do nothing
    return op.toLong() // jump!!!
}

private fun bxc4(regs: List<Long>, op: Int): Long {
    return regs[1] xor regs[2]
}

private fun out5(regs: List<Long>, op: Int): String {
    return (combo(op, regs) % 8).toString()
}

private fun bdv6(regs: List<Long>, op: Int): Long {
    return adv0(regs, op)
}

private fun cdv7(regs: List<Long>, op: Int): Long {
    return adv0(regs, op)
}

private fun combo(op: Int, regs: List<Long>): Long = when {
    op in 0..3 -> op.toLong()
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