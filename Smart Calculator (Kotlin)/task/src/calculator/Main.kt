package calculator

fun main() {

    calculate()

}

fun calculate() {

    val input = Regex(""" +""").split(readln())

    when {
        input.contains("/exit") -> {
            println("Bye!")
        }

        input.contains("/help") -> {
            println("The program calculates the sum of numbers")
            calculate()
        }

        input.first() == "" -> {
            calculate()
        }

        input.size == 1 -> {
            println(input.first())
            calculate()
        }

        else -> {
            var output = input.first().toInt()
            for ((index, value) in input.withIndex()) {
                if (index != 0 && index != input.lastIndex) {
                    if (value.contains("+") || (value.contains("-") && value.length % 2 == 0)) {
                        output += input[index + 1].toInt()
                    } else if (value.contains("-")) {
                        output -= input[index + 1].toInt()
                    }
                }
            }
            println(output)
            calculate()
        }
    }
}
