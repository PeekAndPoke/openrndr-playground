import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noclear.NoClear
import org.openrndr.extra.olive.oliveProgram
import java.time.Instant
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */

fun main() = application {
    configure {
        width = 800
        height = 800
        windowResizable = true
        vsync = true
//        fullscreen = Fullscreen.CURRENT_DISPLAY_MODE
    }


    oliveProgram {
        extend(NoClear())

        var x = 0.0
        var speedX = 3.5
        var y = 0.0
        var speedY = 3.0

        val started = Instant.now()

        extend {
            drawer.fill = ColorRGBa.BLACK.opacify(0.01)
            drawer.rectangle(drawer.bounds)

            drawer.text("x: $x", 20.0, 20.0)
            drawer.text("y: $y", 20.0, 40.0)
            drawer.text("speedX: $speedX", 20.0, 60.0)
            drawer.text("speedZ: $speedY", 20.0, 80.0)

            val millis = (Instant.now().toEpochMilli() - started.toEpochMilli()).toDouble()

            val seconds = millis / 1000

            drawer.fill = ColorRGBa(
                r = abs(sin((seconds) / 40)),
                g = abs(sin((seconds) / 80)), // Random.nextDouble(),
                b = abs(sin((seconds) / 120)), // Random.nextDouble(),
            )

            var ySin1 = abs(sin((x + seconds) / 40) * 100 * cos(seconds / 10.0))

            (0..30).forEach {
                drawer.circle(x, (it * 100) - ySin1, 20.0)
                drawer.circle(drawer.width - x, ((it + 0.5) * 100) - ySin1, 20.0)
            }

            x = x + speedX
            y = abs(sin(x / 40)) * 100

            if (x > drawer.width) {
                x = x % drawer.width
            }
            if (y > drawer.height) {
                y = y % drawer.height
            }
        }
    }
}
