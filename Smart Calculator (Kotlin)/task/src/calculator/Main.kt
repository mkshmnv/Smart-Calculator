package calculator

import java.util.*

class SmartCalculator {

    private lateinit var inputString: String
    private var operands = mutableMapOf<String, Int>()

    // Regex's
    private val letters = Regex("[a-zA-Z]*")
    private val spaces = Regex(""" +""")
    private val commands = Regex("""/.+""")
    private val operators = Regex("[+\\-*/]")
    private val numbers = Regex("[+\\-0-9]+")

    init {
        start()
    }

    // convert infix to postfix and calculate
    private fun calculate(infix: String) {
        var postfix = ""

        val outputQueue = mutableListOf<String>()
        val operatorStack = Stack<String>()
        val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)
        val symbols = Regex("[+\\-*/()]")

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

        while (!operatorStack.isEmpty()) {
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

        println(result)
    }

    // begin
    private fun start() {
        var parentheses: Boolean
        val symbols = Regex("[+\\-*/()=]")
        val inputList = mutableListOf<String>()
        var firstElement : String

        while (true) {
            inputString = readln()
//            inputList = inputString.split(spaces).joinToString("").split("=")

            // TODO fix Invalid expression ----->  3 *** 5 and -10
            inputString.forEach { inputList += if (it.toString().matches(symbols)) " $it " else it.toString() }
            firstElement = inputList.joinToString("").split(" ").first()

            parentheses = inputString.count { it == '(' } != inputString.count { it == ')' }

            when {
                // check parentheses
                parentheses -> {
                    println("Invalid expression")
                    return
                }

                // empty input
                inputString == "" -> continue

                // application of commands
                inputString.matches(commands) -> {
                    when (inputString) {
                        "/exit" -> {
                            println("Bye!")
                            break
                        } // passed
                        "/help" -> println("There should be a description of the calculator's functionality.") // passed
                        else -> println("Unknown command") // passed
                    }
                } // all passed

                // interaction database variables and set a new value
                inputString.contains("=") -> {
                    addVariable()
                    println(operands)
                } // all passed

                inputList.size == 1 -> {
                    when {
                        inputString == "" -> continue
                        firstElement.matches(numbers) -> println(firstElement)
                        !firstElement.matches(letters) -> println("Invalid identifier")
                        // TODO fix Invalid identifier ----->  1 +++ 2 * 3 -- 4
                        !operands.containsKey(firstElement) -> println("Unknown variable")
                        else -> println(operands[firstElement])
                    }
                } // all passed

                // calculation
                inputString.contains(operators) -> {
                    var outputString = ""

                    inputString.split("").forEach {
                        if (operands.containsKey(it)) outputString += operands[it] else outputString += it
                    } // passed

                    calculate(outputString)

                } // all passed

//                output
//                    .replace("+", "")
//                    .replace("--", "")
//                    .replace(Regex("-\\s+"), "-")
//                    .split(Regex("\\s+"))
//                    .sumOf { it.toInt() }

                // calculate
                else -> {
                    if (operands.containsKey(inputString)) {
                        // variable displayed
                        println(operands[inputString])
                    } else {
                        // variable is valid but not declared yet
                        println("Unknown variable")
                    }
                } // all passed
            }
        }
    }

    private fun addVariable() {
        val (key, value) = inputString.replace(Regex(""" +"""), "").split('=')
        val assignment =
            inputString.count { it == '=' } != 1 || (!value.matches(letters) && !value.matches(Regex("[0-9]*")))
        val identifier = !key.matches(letters)
        val variableNotDeclared = value.matches(letters) && !operands.containsKey(value)

        when {
            // name of a variable (identifier) can contain only Latin letters.
            identifier -> println("Invalid identifier") // passed

            // value of a variable is invalid during variable declaration
            assignment -> println("Invalid assignment") // passed

            // The value can be a value of another variable
            operands.containsKey(value) -> operands[key] = operands[value]!!.toInt() // passed

            // variable is valid but not declared yet
            variableNotDeclared -> println("Unknown variable") // passed

            // variable is declared
            else -> operands += mapOf(key to value.toInt()) // passed
        }
    }
}


fun main() {

    SmartCalculator()

}








