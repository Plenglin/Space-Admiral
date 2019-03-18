package xyz.plenglin.spaceadmiral.net.server

import com.badlogic.gdx.graphics.Color
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.net.io.ClientCommand
import xyz.plenglin.spaceadmiral.net.io.CommandResult
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class Server(val players: List<PlayerInterface>, val instance: GameInstance = GameInstance()) {

    private val commands: BlockingQueue<Pair<PlayerInterface, ClientCommand>> = LinkedBlockingQueue()

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

        val commands = mutableListOf<Pair<PlayerInterface, ClientCommand>>()
        this.commands.drainTo(commands)
        commands.forEach { (sender, cmd) ->
            logger.info("Received command {} from {}", cmd, sender)
            val result = cmd.applyCommand(sender, instance)

            when (result) {
                is CommandResult.Success -> {
                    logger.info("Command {} successfully executed", cmd)
                }
                is CommandResult.Fail -> {
                    logger.warn("Command {} failed! Reason: {}", cmd, result.reason)
                }
            }
        }

        logger.debug("Updating GameInstance")
        instance.update()
        players.forEach {
            logger.debug("Sending data to {}", it)
            it.sendGameState(instance.gameState)
        }

    }

    fun onCommandReceived(client: PlayerInterface, command: ClientCommand) {
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