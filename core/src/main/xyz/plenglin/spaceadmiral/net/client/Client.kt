package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.GameState
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Client(server: ServerInterface) {
    private val gsLock = ReentrantLock(true)
    private var gameState: GameState = GameState()
        @Synchronized set
        @Synchronized get

    init {
        server.attachClient(this)
    }

    fun withGameState(f: (GameState) -> Unit) {
        gsLock.withLock {
            f(gameState)
        }
    }

    fun update() {

    }
}