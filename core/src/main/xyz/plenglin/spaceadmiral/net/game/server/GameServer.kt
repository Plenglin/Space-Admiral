package xyz.plenglin.spaceadmiral.net.game.server

import com.badlogic.gdx.graphics.Color
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.TadarData
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.game.io.ClientCommand
import xyz.plenglin.spaceadmiral.net.game.io.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.net.game.io.CommandResult
import xyz.plenglin.spaceadmiral.net.game.io.dto.asDTO
import xyz.plenglin.spaceadmiral.util.IntVector2
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class GameServer(players: List<GamePlayerInterfaceFactory>, val instance: GameInstance = GameInstance()) {

    private val commands: BlockingQueue<Pair<GamePlayerInterface, ClientCommand>> = LinkedBlockingQueue()
    val players: List<GamePlayerInterface>

    private val bos = ByteArrayOutputStream()
    private val oos = ObjectOutputStream(bos)

    init {
        logger.info("Initializing server {}", this)
        this.players = players.mapIndexed { i, pl ->
            val team = instance.gameState.createTeam(COLORS[i], uuid = pl.team)
            pl.createPlayerInterface(team, this)
        }
    }

    fun update() {
        logger.debug("Updating server {}", this)

        val commands = mutableListOf<Pair<GamePlayerInterface, ClientCommand>>()
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

        val serializedSectors = instance.gameState.sectors.map { (k, s) ->
            k to s.asDTO()
        }.toMap()
        val knownSectors = mutableMapOf<Team, HashSet<IntVector2>>()
        instance.gameState.squads.forEach { _, squad ->
            knownSectors.getOrPut(squad.team) { HashSet() }.add(squad.sector.pos)
        }

        players.forEach { player ->
            logger.debug("Sending payload to {}", player)
            val tadar = TadarData()
            tadar.initializeNoise()
            val sentSectors = knownSectors[player.team]?.map { serializedSectors.getValue(it) } ?: listOf()
            val payload = ClientUpdatePayload(sentSectors, tadar)
            player.sendPayload(payload)
        }

    }

    fun onCommandReceived(client: GamePlayerInterface, command: ClientCommand) {
        logger.info("Client {} sent squad command {}", client, command)
        commands.add(client to command)
    }

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(GameServer::class.java)

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