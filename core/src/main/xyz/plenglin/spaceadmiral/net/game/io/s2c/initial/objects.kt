package xyz.plenglin.spaceadmiral.net.game.io.s2c.initial

import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.GameState
import java.io.Serializable

class ClientInitialPayload(val gameState: GameState) : Serializable

fun GameInstance.toInitialDTO(): ClientInitialPayload {
    return ClientInitialPayload(gameState)
}
