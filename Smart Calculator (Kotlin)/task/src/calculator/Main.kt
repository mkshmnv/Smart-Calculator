package calculator

fun main() {

    calculate()

}

fun calculate() {

    val input = Regex(""" +""").split(readln())

    when {
        input.first() == "" -> {
            calculate()
        }

        input.first().toIntOrNull() != null  -> {
            if (input.size == 1) {
                println(input.first().toInt())
                calculate()
            } else {
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

        else -> {
            if (input.first().contains("/")) {
                if (input.contains("/exit")) {
                    println("Bye!")
                } else if (input.contains("/help")) {
                    println("The program calculates the sum of numbers")
                    calculate()
                } else {
                    println("Unknown command")
                    calculate()
                }
            } else {
                println("Invalid expression")
                calculate()
            }
        }
    }
}

