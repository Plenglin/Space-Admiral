package xyz.plenglin.spaceadmiral.net.server

import com.badlogic.gdx.graphics.Color
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.team.Team

class Server(val players: List<PlayerInterface>) {

    val instance = GameInstance()

    init {
        logger.info("Initializing server {}", this)
        players.forEachIndexed { i, pl ->
            pl.attachServer(this)
            val team = Team(instance, COLORS[i])
            pl.team = team
            instance.teams.add(team)
        }
    }

    fun update() {
        logger.debug("Updating server {}", this)
        instance.update()
        val gs = instance.createDTO()
        players.forEach {
            logger.debug("Sending data to {}", it)
            it.sendGameState(gs)
        }
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