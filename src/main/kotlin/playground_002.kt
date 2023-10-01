fun main() {
    val text = "Edgars programm"
    println(text)

    for (x in 1..10) {
        for (y in 1..10) {
            val e = x * y
            println("${x.pad2()} * ${y.pad2()} = ${e.pad2()}")
        }
    }
}

fun Int.pad2() = toString().padStart(2, ' ')
