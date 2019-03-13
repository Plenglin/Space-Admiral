package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.net.io.ClientCommand
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * The client, after initial handshake and initial data
 */
class GameClient(private val server: ServerInterface) {
    private val gsLock = ReentrantLock(true)
    var gameState: GameState? = null
    val team: UUID = server.clientTeam

    init {
        server.attachClient(this)
    }

    fun getShip(uuid: UUID): ShipRef = ShipRef(uuid, this)
    fun getProjectile(uuid: UUID): ProjectileRef = ProjectileRef(uuid, this)
    fun getTeam(uuid: UUID): TeamRef = TeamRef(uuid, this)
    fun getSquad(uuid: UUID): SquadRef = SquadRef(uuid, this)

    fun update() {

    }

    fun sendCommand(cmd: ClientCommand) {
        server.sendCommandToServer(cmd)
    }
}

