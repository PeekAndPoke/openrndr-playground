import kotlin.math.pow

fun main() {

//    val speeds = listOf("fast", "slow")
//    val colors = listOf("blue", "red")
//    val cars = listOf("VW", "Mercedes")
//
//    for (speed in speeds) {
//        for (color in colors) {
//            for (car in cars) {
//                println(
//                    "$speed $color $car"
//                )
//            }
//        }
//    }


    while (true) {
        println("Hallo")
        println("Gib eine Zahl ein:")

        val num1 = readln().toDouble()

        println("Gib ein Rechenzeichen ein (+ - * / % **)")

        val op = readln()

        println("Gib noch eine Zahl ein:")

        val num2 = readln().toDouble()

        val result: Double = if (op == "+") {
            num1 + num2
        } else if (op == "-") {
            num1 - num2
        } else if (op == "*") {
            num1 * num2
        } else if (op == "/") {
            num1 / num2
        } else if (op == "%") {
            num1 % num2
        } else if (op == "**") {
            num1.pow(num2)
        } else {
            println("ERROR: unbekannter Operator $op")
            Double.NaN
        }

        println("$num1 $op $num2 = $result")
    }
}
