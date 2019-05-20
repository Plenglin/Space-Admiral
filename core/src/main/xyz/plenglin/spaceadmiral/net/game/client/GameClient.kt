package xyz.plenglin.spaceadmiral.net.game.client

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.game.io.c2s.ClientCommand
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.view.model.GameStateCM
import xyz.plenglin.spaceadmiral.view.model.TeamCM
import xyz.plenglin.spaceadmiral.view.model.toClientModel
import java.util.concurrent.LinkedBlockingQueue

/**
 * The client, after initial handshake and initial data
 */
class GameClient(server: GameServerInterfaceFactory) {
    private val server: GameServerInterface = server.createServerInterface(this)
    val gameState: GameStateCM = server.awaitInitialPayload().toClientModel()

    val team: TeamCM = gameState[this.server.team]!!

    private val payloadQueue = LinkedBlockingQueue<ClientUpdatePayload>()

    @Synchronized
    fun onReceivePayload(payload: ClientUpdatePayload) {
        //logger.debug("Received update payload: {}", payload)
        payloadQueue.add(payload)
    }

    fun update() {
        server.commitCommandsToServer()
        while (true) {
            val payload = payloadQueue.poll() ?: break
            gameState.update(payload)
        }
    }

    fun sendCommand(cmd: ClientCommand) {
        server.queueCommandToServer(cmd)
    }

    private companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(GameClient::class.java)
    }
}

