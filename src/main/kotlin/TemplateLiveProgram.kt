import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.color.presets.*
import org.openrndr.extra.noclear.NoClear
import org.openrndr.extra.olive.oliveProgram
import kotlin.random.Random

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */

class Ball(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var radius: Double = 10.0,
    var speedX: Double = Random.nextDouble(10.0, 15.0),
    var speedY: Double = Random.nextDouble(10.0, 15.0),
    var color: ColorRGBa = ColorRGBa.GREEN
)

fun main() = application {
    configure {
        width = 800
        height = 800
        windowResizable = true
        vsync = true
//        fullscreen = Fullscreen.CURRENT_DISPLAY_MODE

    }


    oliveProgram {

        val colors = listOf(
//                    ColorRGBa.WHITE,
            ColorRGBa.BLACK,
            ColorRGBa.GREEN,
            ColorRGBa.CYAN,
            ColorRGBa.DARK_SLATE_BLUE,
            ColorRGBa.AQUAMARINE,
            ColorRGBa.GOLD,
            ColorRGBa.DARK_BLUE,
            ColorRGBa.DODGER_BLUE,
            ColorRGBa.DARK_CYAN,
            ColorRGBa.DARK_OLIVE_GREEN,
            ColorRGBa.DARK_SLATE_GRAY,
        )

        val balls = (1..100).map {
            Ball(

                x = drawer.width / 2.0,
                y = drawer.height / 2.0,
                speedX = Random.nextDouble(-10.0, 10.0) * 0.5,
                speedY = Random.nextDouble(-10.0, 10.0) * 0.5,
                radius = 15.0,
                color = ColorRGBa(
                    r = Random.nextDouble(),
                    g = Random.nextDouble(),
                    b = Random.nextDouble(),
                    alpha = 0.900,
                )
            )
        }

        extend(NoClear())

        extend {

            drawer.fill = ColorRGBa.BLACK.opacify(0.1)
            drawer.rectangle(drawer.bounds)


            balls.forEach { ball ->
                drawer.fill = ball.color
                drawer.strokeWeight = 0.0
                drawer.circle(x = ball.x, y = ball.y, radius = ball.radius)

                ball.x += ball.speedX
                ball.y += ball.speedY

                if (ball.x >= drawer.width || ball.x <= 0) {
                    ball.speedX = -ball.speedX
                }

                if (ball.y > drawer.height || ball.y <= 0) {
                    ball.speedY = -ball.speedY
                }
            }
        }
    }
}
