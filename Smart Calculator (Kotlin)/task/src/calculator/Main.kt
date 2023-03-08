package calculator

class SmartCalculator {

    private lateinit var inputString: String
    private lateinit var inputList : List<String>
    private var variables = mutableMapOf<String, Int>()

init {
    calculate()
}

    fun calculate() {

        inputString = readln()
        inputList = Regex(""" +""").split(inputString)


        when {

            inputString.contains("=") -> {
                addVariable(inputString)
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

    private fun addVariable(inputString: String) {

        val (key, value) = inputString.replace(" ", "").split('=')

        // Name of a variable (identifier) can contain only Latin letters.
        if (!Regex("[a-zA-Z]*").matches(key)) {
            println("Invalid identifier")
            calculate()
        }

        if (variables.containsKey(value))  { // The value can be a value of another variable
            variables[key] = variables[value]!!.toInt()
        } else if (!Regex("[0-9]*").matches(value)) { // The value can be an integer number
            println("Invalid assignment")
            calculate()
        } else {
            variables += mapOf(key to value.toInt())
        }
    }
}

fun main() {

    SmartCalculator()

}








