package xyz.plenglin.spaceadmiral.net.game.client

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.game.io.c2s.ClientCommand
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.view.model.GameStateCM
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.model.toClientModel
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * The client, after initial handshake and initial strength
 */
class GameClient(server: GameServerInterfaceFactory) {
    private val gsLock = ReentrantLock(true)
    private val server: GameServerInterface = server.createServerInterface(this)
    val team: UUID = this.server.team

    val gameState: GameStateCM = server.initialPayload.toClientModel()

    //fun getShip(uuid: UUID): ShipRef = ShipRef(uuid, this)
    //fun getProjectile(uuid: UUID): ProjectileRef = ProjectileRef(uuid, this)
    //fun getTeam(uuid: UUID): TeamRef = TeamRef(uuid, this)
    fun getSquad(uuid: UUID): SquadCM = gameState.squads[uuid]!!
    fun getSector(pos: IntVector2): SectorCM = gameState.sectors[pos]!!

    @Synchronized
    fun onReceivePayload(payload: ClientUpdatePayload) {
        //logger.debug("Received update payload: {}", payload)
        gameState.update(payload)
    }

    fun update() {
        server.commitCommandsToServer()
    }

    fun sendCommand(cmd: ClientCommand) {
        server.queueCommandToServer(cmd)
    }

    private companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(GameClient::class.java)
    }
}

