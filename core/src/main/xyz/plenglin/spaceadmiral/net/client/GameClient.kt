package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.GameState
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

    fun update() {

    }
}