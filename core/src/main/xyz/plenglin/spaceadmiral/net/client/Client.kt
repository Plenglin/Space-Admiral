package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.GameState

class Client(val server: ServerInterface) {
    var gameState: GameState = GameState()

    init {
        server.attachClient(this)
    }

    fun update() {

    }
}