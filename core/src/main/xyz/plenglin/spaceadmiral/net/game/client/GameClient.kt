package xyz.plenglin.spaceadmiral.net.game.client

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SectorID
import xyz.plenglin.spaceadmiral.SquadID
import xyz.plenglin.spaceadmiral.net.game.io.c2s.ClientCommand
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.view.model.*

/**
 * The client, after initial handshake and initial data
 */
class GameClient(server: GameServerInterfaceFactory) {
    private val server: GameServerInterface = server.createServerInterface(this)
    val gameState: GameStateCM = server.awaitInitialPayload().toClientModel()

    val team: TeamCM = gameState[this.server.team]!!

    //fun getShip(uuid: UUID): ShipRef = ShipRef(uuid, this)
    //fun getProjectile(uuid: UUID): ProjectileRef = ProjectileRef(uuid, this)
    //fun getTeam(uuid: UUID): TeamRef = TeamRef(uuid, this)
    fun getSquad(uuid: SquadID): SquadCM = gameState[uuid]!!
    fun getSector(pos: SectorID): SectorCM? = gameState[pos]

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

