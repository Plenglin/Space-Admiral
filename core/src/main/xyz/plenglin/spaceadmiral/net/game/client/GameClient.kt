package xyz.plenglin.spaceadmiral.net.game.client

import org.apache.commons.lang3.SerializationUtils
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.TadarData
import xyz.plenglin.spaceadmiral.net.game.io.c2s.ClientCommand
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.view.model.GameStateCM
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * The client, after initial handshake and initial strength
 */
class GameClient(server: GameServerInterfaceFactory) {
    private val gsLock = ReentrantLock(true)
    private val server: GameServerInterface = server.createServerInterface(this)
    val team: UUID = this.server.team

    val gameState: GameStateCM = TODO()
    //var gameState: GameState? = null
    var tadarData: TadarData? = null

    //fun getShip(uuid: UUID): ShipRef = ShipRef(uuid, this)
    //fun getProjectile(uuid: UUID): ProjectileRef = ProjectileRef(uuid, this)
    //fun getTeam(uuid: UUID): TeamRef = TeamRef(uuid, this)
    fun getSquad(uuid: UUID): SquadCM = TODO()//SquadRef(uuid, this)
    fun getSector(pos: IntVector2): SectorCM = TODO()//SectorRef(pos, this)

    fun onReceivePayload(payload: ClientUpdatePayload) {
        //gameState = SerializationUtils.deserialize(payload.gameStateBytes)
        gameState.update(payload)
        //tadarData = payload.tadar
    }

    fun update() {
        server.commitCommandsToServer()
    }

    fun sendCommand(cmd: ClientCommand) {
        server.queueCommandToServer(cmd)
    }
}

