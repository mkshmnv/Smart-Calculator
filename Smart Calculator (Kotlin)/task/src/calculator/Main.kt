package calculator

fun main() {
    val input = readln().split(" ").map { it.toInt() }
    println(input.sum())
}
