package calculator

class SmartCalculator {

    private lateinit var inputString: String
    val regex = Regex(""" +""")
    private lateinit var inputList : List<String>

    private var variables = mapOf<String, Int>()

init {
    calculate()
}

    fun calculate() {

        inputString = readln()
        inputList = Regex(""" +""").split(inputString)


        when {

            inputString.contains("=") -> {
                variables += variableAdd(inputString)
                println(variables)
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

    private fun variableAdd(inputString: String): Map<String, Int> {

        val (key, value) = inputString.replace(" ", "").split('=')

        if (!Regex("[a-zA-Z]*").matches(key)) {
            println("Invalid identifier")
            calculate()
        }

        if (!Regex("[0-9]*").matches(value)) {
            println("Invalid assignment")
            calculate()
        }

        return mapOf(key to value.toInt())
    }
}

fun main() {

    SmartCalculator()

}








