package calculator

class SmartCalculator {

    private lateinit var inputString: String
    private lateinit var inputList: List<String>
    private lateinit var firstElement : String
    private var variables = mutableMapOf<String, Int>()


    // Regex's
    private val letters = Regex("[a-zA-Z]*")
    private val spaces = Regex(""" +""")
    private val commands = Regex("""/.+""")
    private val numbers = Regex("[0-9]*")
    private val invalidSymbols = Regex("""(\d+\++|\d+-+|[a-zA-Z]+)+""") // TODO FIX THIS

    init {
        calculate()
    }

    private fun calculate() {

        while (true) {
            inputString = readln()
            inputList = inputString.split(spaces).joinToString("").split("=")
            firstElement = inputList.first()

            when {
                inputString.matches(commands) -> {
                    when (inputString) {
                        "/exit" -> {
                            println("Bye!")
                            break
                        } // passed
                        "/help" -> println("FAQ") // passed
                        else -> println("Unknown command") // passed
                    }
                } // all passed

                inputString.contains("=") -> {
                    addVariable()
                    println(variables)
                }

                inputList.size == 1 -> {
                    when {
                        inputString == "" -> continue
                        !firstElement.matches(letters) -> println("Invalid identifier")
                        !variables.containsKey(firstElement) -> println("Unknown variable")
                        else -> println(variables[firstElement])
                    }
                } // all passed

                // TODO FIX THIS
//                inputString.contains(invalidSymbols) -> println("Invalid expression")

                // calculate
                else -> println(inputString
                    .replace("+", "")
                    .replace("--", "")
                    .replace(Regex("-\\s+"), "-")
                    .split(Regex("\\s+"))
                    .sumOf { it.toInt() })
            }
        }
    }

    private fun addVariable() {
        val (key, value) = inputString.replace(spaces, "").split('=')
        val assignment = inputString.count { it == '=' } > 1 //|| !value.matches(numbers)

        when {
            !firstElement.matches(letters) -> println("Invalid identifier")
            assignment -> println("Invalid assignment")
            else -> {
                if (variables.containsKey(value)) { // The value can be a value of another variable
                    variables[key] = variables[value]!!.toInt()
                } else {
                    if (value.matches(letters)) {
                        println("Unknown variable")
                    } else {
                        // TODO fix exceptions -> test n = a2a
                        variables += mapOf(key to value.toInt())
                    }
                }
            }
        }
    }
}


fun main() {

    SmartCalculator()

}








