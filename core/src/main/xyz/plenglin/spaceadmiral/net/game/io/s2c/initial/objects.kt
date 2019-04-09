package xyz.plenglin.spaceadmiral.net.game.io.s2c.initial

import xyz.plenglin.spaceadmiral.game.GameInstance

fun GameInstance.toInitialDTO(): ClientInitialPayload {
    return ClientInitialPayload(gameState)
}
