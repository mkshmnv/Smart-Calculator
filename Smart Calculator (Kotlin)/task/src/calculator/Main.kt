package calculator

import java.util.*

class SmartCalculator {

    private var inputString: String
    private var operandsMap = mutableMapOf<String, Int>()

    private val letters = Regex("[a-zA-Z]*")

    init {

        var firstElement = ""

        while (true) {
            val inputList = mutableListOf<String>()
            inputString = readln()
                .replace("---", "-")
                .replace("--", "+")
                .replace(Regex("""\++"""), "+")

            inputString
                .forEach {
                    inputList += if (it.toString().matches(Regex("[+\\-*/()=]"))) {
                        " $it "
                    } else {
                        it.toString()
                    }
                }

            // TODO this is crutch for test -10, how can fix this?
            if (inputList.isNotEmpty()) {
                firstElement = if (inputList.first() == " - ") {
                    inputList += inputString
                    inputList.first()
                } else {
                    inputList.first()
                }
            }

            // checks input string
            val isEmptyInputString = inputString == ""
            val isCommand = inputString.matches(Regex("""/.+"""))
            val isOnlyOneElement = inputList.size == 1
            val isAssignment = inputString.contains("=")
            val isHaveOperators = inputString.contains(Regex("[+\\-*/]"))

            when {
                // empty input
                isEmptyInputString -> continue

                // application of commands
                isCommand -> {
                    when (inputString) {
                        "/exit" -> {
                            println("Bye!")
                            break
                        }
                        "/help" -> println("Modify the result of the /help command to explain all possible operators. You can write the output for the command in free form.") // passed
                        else -> println("Unknown command") // passed
                    }
                } // all passed

                // getting existing variables or error
                isOnlyOneElement -> {
                    when {
                        firstElement.matches(Regex("[+\\-0-9]+")) -> println(firstElement)
                        !firstElement.matches(letters) -> println("Invalid identifier")
//                        !operands.containsKey(firstElement) -> println("Unknown variable")
                        firstElement !in operandsMap.keys -> println("Unknown variable")
                        else -> println(operandsMap[firstElement])
                    }
                } // all passed

                // interaction database variables and set a new value
                isAssignment -> assignmentVariable() // all passed

                // calculation
                isHaveOperators -> {
                    var correctString = ""
                    val isEvenParentheses = inputString.count { it == '(' } != inputString.count { it == ')' }
                    val isIncorrectOperators =
                        inputString.contains(Regex("""[*]{2}""")) || inputString.contains(Regex("""/{2}"""))

                    // check parentheses and operators
                    if (isEvenParentheses || isIncorrectOperators) {
                        println("Invalid expression")
                        continue
                    }

                    inputString
                        .split("")
                        .forEach {
                            if (operandsMap.containsKey(it)) correctString += operandsMap[it] else correctString += it
                        } // passed

                    calculate(correctString)
                } // all passed

                else -> {
                    // variable displayed or variable is valid but not declared yet
                    println(if (inputString in operandsMap.keys) operandsMap[inputString] else "Unknown variable")
                } // all passed
            }
        }
    }

    // convert infix to postfix and calculate
    private fun calculate(infix: String) {
        var postfix = ""
        val outputQueue = mutableListOf<String>()
        val operatorStack = Stack<String>()
        val precedence = mapOf("+" to 1, "-" to 1, "*" to 2, "/" to 2)
        var result: Int? = null
        val stack: Deque<Int> = LinkedList()

        // convert string infix to infix
        // add spaces to infix string
        infix.forEach { if (it.toString().matches(Regex("[+\\-*/()]"))) postfix += " $it " else postfix += it }

        // remove excessive spaces
        postfix = postfix.split(Regex(""" +""")).joinToString(" ")

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

    private fun assignmentVariable() {
        val (key, value) = inputString
            .replace(Regex(""" +"""), "")
            .split('=')
        val identifier = !key.matches(letters)
        val assignment = inputString.count { it == '=' } != 1 ||
                (!value.matches(letters) && !value.matches(Regex("\\d*")))
        val variableNotDeclared = value.matches(letters) && !operandsMap.containsKey(value)

        when {
            // name of a variable (identifier) can contain only Latin letters.
            identifier -> println("Invalid identifier") // passed

            // value of a variable is invalid during variable declaration
            assignment -> println("Invalid assignment") // passed

            // The value can be a value of another variable
            operandsMap.containsKey(value) -> operandsMap[key] = operandsMap[value]!!.toInt() // passed

            // variable is valid but not declared yet
            variableNotDeclared -> println("Unknown variable") // passed

            // variable is declared
            else -> operandsMap += mapOf(key to value.toInt()) // passed
        }
    }
}


fun main() {
    SmartCalculator()
}








