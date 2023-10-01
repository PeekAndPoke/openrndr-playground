import java.math.BigInteger

fun main() {

    println("Short: ${Short.MIN_VALUE} to ${Short.MAX_VALUE}")
    println("Int: ${Int.MIN_VALUE} to ${Int.MAX_VALUE}")
    println("Long: ${Long.MIN_VALUE} to ${Long.MAX_VALUE}")
    println("Float: ${Float.MIN_VALUE} to ${Float.MAX_VALUE}")
    println("Double: ${Double.MIN_VALUE} to ${Double.MAX_VALUE}")

    var x: BigInteger = 1.toBigInteger()

    while (true) {
        x = (x * 2.toBigInteger())

        println(x)
        println()
        readln()
    }
}

