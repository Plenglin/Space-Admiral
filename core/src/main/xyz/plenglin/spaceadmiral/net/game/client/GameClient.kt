package xyz.plenglin.spaceadmiral.net.game.client

import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.net.game.io.ClientCommand
import xyz.plenglin.spaceadmiral.util.IntVector2
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * The client, after initial handshake and initial data
 */
class GameClient(server: GameServerInterfaceFactory) {
    private val gsLock = ReentrantLock(true)
    var gameState: GameState? = null
    private val server: GameServerInterface = server.createServerInterface(this)
    val team: UUID = this.server.team

    fun getShip(uuid: UUID): ShipRef = ShipRef(uuid, this)
    fun getProjectile(uuid: UUID): ProjectileRef = ProjectileRef(uuid, this)
    fun getTeam(uuid: UUID): TeamRef = TeamRef(uuid, this)
    fun getSquad(uuid: UUID): SquadRef = SquadRef(uuid, this)
    fun getSector(pos: IntVector2): SectorRef = SectorRef(pos, this)

    fun update() {
        server.commitCommandsToServer()
    }

    fun sendCommand(cmd: ClientCommand) {
        server.queueCommandToServer(cmd)
    }
}

