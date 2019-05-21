package xyz.plenglin.spaceadmiral.net.game.io.s2c.initial

import xyz.plenglin.spaceadmiral.game.GameState
import java.io.Serializable

data class ClientInitialPayload(val gameState: GameState) : Serializable

fun GameState.toInitialDTO(): ClientInitialPayload {
    return ClientInitialPayload(this)
}
