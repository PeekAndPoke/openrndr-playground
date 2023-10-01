import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.extra.color.presets.BLUE_STEEL
import org.openrndr.extra.color.presets.LIGHT_BLUE
import org.openrndr.extra.color.presets.STEEL_BLUE
import org.openrndr.extra.noclear.NoClear
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.math.Vector2
import java.time.Instant
import kotlin.math.sqrt
import kotlin.random.Random

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */

data class Enemy(
    val position: Vector2,
    val speed: Vector2,
    val radius: Double,
    val type: Type,
) {
    enum class Type {
        Enemy,
        Friend,
    }
}

data class Player(
    val position: Vector2,
    val target: Vector2 = position,
    val radius: Double,
)

sealed class Mode {
    object Welcome : Mode()

    data class Game(
        val started: Instant = Instant.now(),
        var score: Int = 0,
        var lives: Int,
        val player: Player,
        var enemies: List<Enemy> = listOf(),
    ) : Mode()

    data class GameOver(
        val started: Instant = Instant.now(),
        val score: Int,
        val timeScore: Long,
        val player: Player,
    ) : Mode()
}

val random = Random

fun main() = application {
    configure {
        width = 800
        height = 800
        windowResizable = true
        vsync = true
//        fullscreen = Fullscreen.CURRENT_DISPLAY_MODE
    }

    oliveProgram {

        val font = loadFont("data/fonts/default.otf", 24.0)

        var mode: Mode = Mode.Welcome

        mouse.buttonDown.listen { mouse ->
            when (val m = mode) {
                is Mode.Welcome -> {
                    mode = Mode.Game(
                        lives = 5,
                        player = Player(
                            position = window.size / 2.0,
                            radius = 20.0,
                        )
                    )
                }

                is Mode.Game -> {
                    mode = m.copy(
                        player = m.player.copy(
                            target = mouse.position
                        )
                    )
                }

                is Mode.GameOver -> {
                    if (Instant.now().epochSecond - m.started.epochSecond > 3) {
                        mode = Mode.Welcome
                    }
                }
            }
        }

        extend(NoClear())

        extend {
            drawer.fontMap = font

            drawer.fill = ColorRGBa.BLACK.opacify(0.05)
            drawer.rectangle(drawer.bounds)

            when (val m = mode) {
                is Mode.Welcome -> renderWelcome()
                is Mode.Game -> mode = renderGame(m)
                is Mode.GameOver -> renderGameOver(m)
            }
        }
    }
}

fun Program.renderWelcome() {
    drawer.fill = ColorRGBa.WHITE
    drawer.text("Click to start", 10.0, 20.0)
}

fun Program.renderGameOver(mode: Mode.GameOver) {
    drawer.fill = ColorRGBa.WHITE
    drawer.text("Game Over", 10.0, 20.0)
    drawer.text("Score ${mode.score} points", 10.0, 40.0)
    drawer.text("time Score ${mode.timeScore} seconds", 10.0, 60.0)
    drawer.circle(mode.player.position, 20.0)
}

fun Program.renderGame(mode: Mode.Game): Mode {
    var enemies = mode.enemies
    var player = mode.player
    var score = mode.score
    var lives = mode.lives

    val now = Instant.now()
    val seconds = now.epochSecond - mode.started.epochSecond

    if (random.nextInt(0, 1000) > 1000 - (seconds + 50)) {
        val maxXSpeed = sqrt(score.toDouble() / 1000.0) + 0.01

        enemies = enemies.plus(
            Enemy(
                position = Vector2(
                    x = random.nextDouble(0.0, window.size.x),
                    y = 0.0,
                ),
                speed = Vector2(
                    x = random.nextDouble(-maxXSpeed, maxXSpeed),
                    y = random.nextDouble(0.1, 1.0),
                ),
                radius = 30.0,
                type = if (random.nextInt(0, 100) > 95) {
                    Enemy.Type.Friend
                } else {
                    Enemy.Type.Enemy
                }
            )
        )
    }

//            drawer.text("Position: $position", 10.0, 10.0)
//            drawer.text("Target:   $target", 10.0, 30.0)

    // Draw player
    drawer.fill = ColorRGBa.LIGHT_BLUE
    drawer.circle(position = player.position, radius = player.radius)

    // Move player
    val distance = (player.target - player.position)
    val movement = distance / 10.0
    player = player.copy(position = player.position + movement)

    // Draw enemies
    enemies.forEach { enemy ->
        when (enemy.type) {
            Enemy.Type.Enemy -> {
                drawer.fill = ColorRGBa.RED
                drawer.circle(position = enemy.position, radius = enemy.radius)
            }

            Enemy.Type.Friend -> {
                drawer.fill = ColorRGBa.BLUE_STEEL
                drawer.circle(position = enemy.position, radius = enemy.radius)
            }
        }
    }

    // Move enemies
    enemies = enemies.map { enemy ->
        enemy.copy(
            position = enemy.position + enemy.speed,
            speed = enemy.speed.copy(y = enemy.speed.y * 1.03),
        )
    }

    // Find enemies that have to be removed
    val removedEnemies = enemies.filter { enemy ->
        enemy.position.y > window.size.y
    }
    // Remove enemies
    enemies = enemies.minus(removedEnemies.toSet())
    // Increase score
    score += removedEnemies.sumOf { it.radius }.toInt()

    // Detect collisions
    val collisionsWithFriends = enemies
        .filter { it.type == Enemy.Type.Friend }
        .filter { enemy -> (enemy.position - player.position).length < (enemy.radius + player.radius) }

    val collisionsWithEnemies = enemies
        .filter { it.type == Enemy.Type.Enemy }
        .filter { enemy -> (enemy.position - player.position).length < (enemy.radius + player.radius) }

    if (collisionsWithFriends.isNotEmpty()) {
        lives += 1
        score += 1000
        drawer.fill = ColorRGBa.STEEL_BLUE
        drawer.rectangle(Vector2.ZERO, width = window.size.x, height = window.size.y)
    }

    if (collisionsWithEnemies.isNotEmpty()) {
        lives -= 1
        score -= 100
        drawer.fill = ColorRGBa.RED
        drawer.rectangle(Vector2.ZERO, width = window.size.x, height = window.size.y)
    }

    // Remove colliding from enemies
    enemies = enemies
        .minus(collisionsWithFriends.toSet())
        .minus(collisionsWithEnemies.toSet())

    // Draw text
    drawer.fill = ColorRGBa.WHITE
    drawer.text("Time: $seconds", 10.0, 20.0)
    drawer.text("Score: $score", 10.0, 40.0)
    drawer.text("Lives: $lives", 10.0, 60.0)


    // Game over?
    return if (lives <= 0) {
        Mode.GameOver(
            score = score,
            timeScore = seconds,
            player = player,
        )
    } else {
        mode.copy(
            score = score,
            lives = lives,
            player = player.copy(
                target = mouse.position
            ),
            enemies = enemies
        )
    }
}
