package calculator

fun main() {

    calculate()

}

fun calculate() {
    val input = readln().split(" ")

    if (input[0] == "/exit") {
        println("Bye!")
    } else if (input[0] == "") {
        calculate()
    } else if (input.size == 1) {
        println(input[0])
        calculate()
    } else {
        println(input[0].toInt() + input[1].toInt())
        calculate()
    }
}
