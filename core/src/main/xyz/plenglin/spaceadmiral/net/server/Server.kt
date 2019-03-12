package xyz.plenglin.spaceadmiral.net.server

import com.badlogic.gdx.graphics.Color
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.net.io.Command
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class Server(val players: List<PlayerInterface>, val instance: GameInstance = GameInstance()) {

    private val commands: BlockingQueue<Pair<PlayerInterface, Command>> = LinkedBlockingQueue()

    init {
        logger.info("Initializing server {}", this)
        players.forEachIndexed { i, pl ->
            pl.attachServer(this)
            val team = instance.gameState.createTeam(COLORS[i])
            pl.team = team
        }
    }

    fun update() {
        logger.debug("Updating server {}", this)

        val commands = mutableListOf<Pair<PlayerInterface, Command>>()
        this.commands.drainTo(commands)
        commands.forEach { (sender, cmd) ->
            logger.info("Received command {} from {}", cmd, sender)
            if (cmd.isPermitted(sender)) {
                logger.info("Permitting command {} from {}", cmd, sender)
                cmd.applyCommand(instance)
            } else {
                logger.warn("{} is not allowed to send {}!", sender, cmd)
            }
        }

        instance.update()
        players.forEach {
            logger.debug("Sending data to {}", it)
            it.sendGameState(instance.gameState)
        }

    }

    fun onCommandReceived(client: PlayerInterface, command: Command) {
        logger.info("Client {} sent squad command {}", client, command)
        commands.add(client to command)
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