package calculator

fun main() {

    calculate()

}

fun calculate() {
    val input = readln().split(" ")

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
            input.filter { it }
            var temp = 0

            for (i in input) {
                if (i.toInt() in -9..9) {
                    temp = i.toInt()
                }
            }



            println(input.filter { it != " " }.sumOf { it.toInt() })
            calculate()
        }
    }
}
