package calculator

import java.util.*

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
        calculate11()
    }

    // convert infix to postfix and calculate
    private fun calculate(infix: String): Int? {
        var postfix = ""

        val outputQueue = mutableListOf<String>()
        val operatorStack = Stack<String>()
        val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)
        val symbols = Regex("[+\\-*/^()]")

        var result: Int? = null
        val stack: Deque<Int> = LinkedList()

        // convert string infix to infix
        // add spaces to infix string
        infix.forEach { if (it.toString().matches(symbols)) postfix += " $it " else postfix += it }

        // remove excessive spaces
        postfix = postfix.split(spaces).joinToString(" ")

        if (postfix.last() == ' ') postfix = postfix.dropLast(1)

        // convert infix string to postfix string
        postfix.split(" ").forEach { token ->
            when {
                token.matches(Regex("-?\\d+(\\.\\d+)?")) -> outputQueue.add(token)
                token == "(" -> operatorStack.push(token)
                token == ")" -> {
                    while (operatorStack.peek() != "(") {
                        outputQueue.add(operatorStack.pop())
                    }
                    operatorStack.pop()
                }
                token in precedence.keys -> {
                    while (!operatorStack.isEmpty() && operatorStack.peek() != "(" &&
                        precedence[operatorStack.peek()]!! >= precedence[token]!!) {
                        outputQueue.add(operatorStack.pop())
                    }
                    operatorStack.push(token)
                }
                else -> throw IllegalArgumentException("Invalid token: $token")
            }
        }

        // check parentheses
        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() == "(" || operatorStack.peek() == ")") {
                throw IllegalArgumentException("Invalid expression")
            }
            outputQueue.add(operatorStack.pop())
        }

        postfix = outputQueue.joinToString(" ")

        // calculate postfix expression
        if (postfix.isNotEmpty()) {
            var num: Int? = null

            for (c in postfix.toCharArray()) {

                when {
                    c == ' ' -> {
                        if (num != null) {
                            stack.push(num)
                            num = null
                        }
                    }
                    c - '0' in 0..9 -> {
                        val digit = c - '0'
                        num = if (num == null) digit else num * 10 + digit
                    }
                    else -> {
                        val num2: Int = stack.pop()
                        val num1: Int = stack.pop()

                        when (c) {
                            '+' -> stack.push(num1 + num2)
                            '-' -> stack.push(num1 - num2)
                            '*' -> stack.push(num1 * num2)
                            '/' -> stack.push(num1 / num2)
                        }
                    }
                }
            }
            result = stack.pop()
        }

        return result
    }

    private fun calculate11() {

        while (true) {
            inputString = readln()
            inputList = inputString.split(spaces).joinToString("").split("=")
            firstElement = inputList.first()

            when {
                // application of commands
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

                // interaction database variables
                inputString.contains("=") -> {
                    addVariable()
                    println(variables)
                } // all passed

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
                else -> calculate(inputString)
//                    println(inputString
//                    .replace("+", "")
//                    .replace("--", "")
//                    .replace(Regex("-\\s+"), "-")
//                    .split(Regex("\\s+"))
//                    .sumOf { it.toInt() })
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
    lateinit var inputString: String
    val variables = mutableMapOf<String, Int>()

    while (true) {
        inputString = readln()

        when {
            // empty input
            inputString == "" -> continue

            // program commands
            inputString.matches(Regex("""/.+""")) -> {
                when (inputString) {
                    "/exit" -> {
                        println("Bye!")
                        break
                    } // passed
                    "/help" -> println("There should be a description of the calculator's functionality.") // passed
                    else -> println("Unknown command") // passed
                }
            } // all passed

            // set a new value
            inputString.contains('=') -> {
                val letters = Regex("[a-zA-Z]*")

                val (key, value) = inputString.replace(Regex(""" +"""), "").split('=')

                val assignment =
                    inputString.count { it == '=' } != 1 || (!value.matches(letters) && !value.matches(Regex("[0-9]*")))
                val identifier = !key.matches(letters)
                val variableNotDeclared = value.matches(letters) && !variables.containsKey(value)

                when {
                    // name of a variable (identifier) can contain only Latin letters.
                    identifier -> println("Invalid identifier") // passed

                    // value of a variable is invalid during variable declaration
                    assignment -> println("Invalid assignment") // passed

                    // The value can be a value of another variable
                    variables.containsKey(value) -> variables[key] = variables[value]!!.toInt() // passed

                    // variable is valid but not declared yet
                    variableNotDeclared -> println("Unknown variable") // passed

                    // variable is declared
                    else -> variables += mapOf(key to value.toInt()) // passed
                }
            } // all passed

            // calculation
            inputString.contains(Regex("[+\\-*/]+")) -> {
                var output = ""

                inputString.split("").forEach {
                    if (variables.containsKey(it)) output += variables[it] else output += it
                } // passed

                println(output
                    .replace("+", "")
                    .replace("--", "")
                    .replace(Regex("-\\s+"), "-")
                    .split(Regex("\\s+"))
                    .sumOf { it.toInt() }
                ) // passed
            } // all passed

            // variable displayed
            else -> {
                if (variables.containsKey(inputString)) {
                    // variable displayed
                    println(variables[inputString])
                } else {
                    // variable is valid but not declared yet
                    println("Unknown variable")
                }
            } // all passed
        }
    }
}








