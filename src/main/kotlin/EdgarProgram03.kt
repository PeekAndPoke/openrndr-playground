import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.LineCap
import org.openrndr.extra.noclear.NoClear
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import kotlin.random.Random

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */
data class Dot(
    var pos: Vector2,
    var speed: Vector2,
) {
    fun move(windowSize: Vector2) {
        pos += speed

        if (pos.x <= 0 || pos.x >= windowSize.x) {
            speed = speed.copy(x = -speed.x)
        }

        if (pos.y <= 0 || pos.y >= windowSize.y) {
            speed = speed.copy(y = -speed.y)
        }
    }
}

fun main() = application {
    configure {
        width = 1920
        height = 1200
        windowResizable = true
        vsync = true
//        fullscreen = Fullscreen.CURRENT_DISPLAY_MODE
    }

    oliveProgram {
        val random = Random

        val dots = (1..2).map {
            Dot(
                pos = Vector2(
                    x = window.size.x / 2,
                    y = window.size.y / 2,
                ),
                speed = Vector2(
                    x = random.nextDouble(-3.0, 3.0),
                    y = random.nextDouble(-3.0, 3.0),
                ),
            )
        }

        extend(NoClear())

        extend {
            drawer.fill = ColorRGBa.BLACK.opacify(0.02)
            drawer.rectangle(drawer.bounds)

            dots.forEach {
//                drawer.circle(it.pos,15.0)
                it.move(window.size)
            }

            drawer.stroke = ColorRGBa.WHITE
            drawer.strokeWeight = 3.0
            drawer.lineCap = LineCap.ROUND

            dots.zipWithNext { d1, d2 ->
                drawer.lineSegment(d1.pos, d2.pos)
            }

            drawer.lineSegment(
                dots.first().pos,
                dots.last().pos,
            )
        }
    }
}
