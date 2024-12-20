package advent.d10

fun main() {
    val nums = listOf(1, 2, 3, 4)
    println(recursiveSum(nums))
}

fun recursiveSum(nums: List<Int>): Int {
    if (nums.isEmpty()) return 0
    return nums.last() + recursiveSum(nums.dropLast(1))
}