package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.GameState
import java.util.concurrent.locks.ReentrantLock

/**
 * The client, after initial handshake and initial data
 */
class GameClient(private val server: ServerInterface) {
    private val gsLock = ReentrantLock(true)
    var gameState: GameState? = null

    init {
        server.attachClient(this)
    }

    fun update() {

    }
}