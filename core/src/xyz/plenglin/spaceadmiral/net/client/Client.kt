package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.GameState

class Client(val server: ServerInterface) {
    val gameState: GameState = GameState()
}