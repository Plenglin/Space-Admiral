package xyz.plenglin.spaceadmiral.net.game.server

import com.badlogic.gdx.graphics.Color
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.TadarData
import xyz.plenglin.spaceadmiral.game.TadarData.Companion.VISIBILITY_THRESHOLD
import xyz.plenglin.spaceadmiral.game.sector.Sector
import xyz.plenglin.spaceadmiral.net.game.io.c2s.ClientCommand
import xyz.plenglin.spaceadmiral.net.game.io.c2s.CommandResult
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.asUpdateDTO
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class GameServer(vararg players: GamePlayerInterfaceFactory, val gameState: GameState = GameState()) {

    private val commands: BlockingQueue<Pair<GamePlayerInterface, ClientCommand>> = LinkedBlockingQueue()
    val players: List<GamePlayer>

    init {
        logger.info("Initializing server {}", this)
        this.players = players.mapIndexed { i, pl ->
            val team = gameState.createTeam(COLORS[i], uuid = pl.team)
            GamePlayer(pl.createPlayerInterface(team, this))
        }
    }

    fun update() {
        logger.trace("Updating server {}", this)

        players.forEach { player ->
            player.occupied.clear()
            player.team.squads.forEach {
                val sector = it.sector
                if (sector != null) {
                    player.occupied.add(sector)
                }
            }
        }

        applyCommands()

        logger.trace("Updating GameInstance")
        gameState.update()

        sendToClients()
    }

    private fun applyCommands() {
        val commands = mutableListOf<Pair<GamePlayerInterface, ClientCommand>>()
        this.commands.drainTo(commands)
        commands.forEach { (sender, cmd) ->
            logger.info("Received command {} from {}", cmd, sender)

            when (val result = cmd.applyCommand(sender, gameState)) {
                is CommandResult.Success -> {
                    logger.info("Command {} successfully executed", cmd)
                }
                is CommandResult.Fail -> {
                    logger.warn("Command {} failed! Reason: {}", cmd, result.reason)
                }
            }
        }
    }

    private fun sendToClients() {
        players.forEach { player ->
            sendToPlayer(player)
        }
    }

    private fun sendToPlayer(player: GamePlayer) {
        logger.trace("Sending payload to {}", player)
        val tadar = TadarData()
        tadar.initializeNoise()

        val occupiedSectors = player.occupied
        //println(occupiedSectors)
        val signals = gameState.sectors.map { it to 0f }.toMap().toMutableMap()

        occupiedSectors.forEach { s1 ->
            val strength = s1.squads.values
                    .filter { player.team.isAlliedWith(it.team) }
                    .map { it.template.tadarStrength }
                    .sum()
            signals[s1] = 1f

            (signals.keys - occupiedSectors).forEach { s2 ->
                val dist2 = s1.pos.dist2(s2.pos)
                val signature = s1.squads.values
                        .map { it.template.tadarSignature }
                        .sum()
                signals[s2] = signals[s2]!!.plus(strength * signature / dist2)
            }
        }

        val knownSectors = mutableSetOf<Sector>()

        signals.forEach { (sector, signal) ->
            tadar[sector.pos] += signal
            if (signal >= VISIBILITY_THRESHOLD) {
                knownSectors.add(sector)
            }
        }

        val warping = gameState.warpBubbles.map { it.value.asUpdateDTO(gameState.time) }
        val sentSectors = knownSectors.map { it.asUpdateDTO() }
        val payload = ClientUpdatePayload(sentSectors, warping, listOf(), tadar)
        player.iface.sendPayload(payload)
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
                Color.GREEN,
                Color.YELLOW,
                Color.PINK,
                Color.WHITE
        )

    }
}