package xyz.plenglin.spaceadmiral.net.server

import com.badlogic.gdx.graphics.Color
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.team.Team
import kotlin.random.Random

class Server(val players: List<PlayerInterface>) {

    val instance = GameInstance()

    init {
        players.forEachIndexed { i, it ->
            instance.teams.add(Team(instance, COLORS[i]))
        }
    }

    fun update() {
        logger.info("Updating server")
        instance.update()
    }

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(Server::class.java)

        @JvmStatic
        val COLORS = listOf<Color>(
                Color.BLUE,
                Color.RED,
                Color.CYAN,
                Color.GREEN,
                Color.YELLOW,
                Color.PINK,
                Color.WHITE
        )

    }
}