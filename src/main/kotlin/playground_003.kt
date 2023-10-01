import kotlin.math.pow


fun main() {

    println("gib eine Zahl ein.")
    val x = readln().toDouble()

    println("gib noch eine Zahl ein.")
    val y = readln().toDouble()

//    val t =x + y
    println("$x + $y = ${x + y}")
    println("$x - $y = ${x - y}")
    println("$x * $y = ${x * y}")
    println("$x / $y = ${x / y}")
    println("$x ^ $y = ${x.pow(y)}")
}

