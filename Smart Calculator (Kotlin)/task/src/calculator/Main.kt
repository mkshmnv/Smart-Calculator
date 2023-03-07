package calculator



fun main() {

    calculate()

}



var variablesMap = mapOf<Char, Int>()



fun calculate() {

    val inputString = readln()
    val inputList = Regex(""" +""").split(inputString)


    when {

        inputString.contains("=") -> {
            variablesMap += variableAdd(inputString)
            println(variablesMap)
            calculate()
        }

        inputList.first() == "" -> {
            calculate()
        }

        inputList.first().toIntOrNull() != null  -> {
            if (inputList.size == 1) {
                println(inputList.first().toInt())
                calculate()
            } else {
                var output = inputList.first().toInt()
                for ((index, value) in inputList.withIndex()) {
                    if (index != 0 && index != inputList.lastIndex) {
                        if (value.contains("+") || (value.contains("-") && value.length % 2 == 0)) {
                            output += inputList[index + 1].toInt()
                        } else if (value.contains("-")) {
                            output -= inputList[index + 1].toInt()
                        }
                    }
                }
                println(output)
                calculate()
            }
        }

        else -> {
            if (inputList.first().contains("/")) {
                if (inputList.contains("/exit")) {
                    println("Bye!")
                } else if (inputList.contains("/help")) {
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

fun variableAdd(inputString: String): Map<Char, Int> {
    val (key, value) = inputString.filterNot { it == ' ' }.split("=")

    if (!key.single().isLetter()) {
        println("Invalid identifier")
        calculate()
    }

    if (inputString.count { it == '='} > 1 || value.toIntOrNull() == null) {
        println("Invalid assignment")
        calculate()
    }

    return mapOf(key.single() to value.toInt())
}

