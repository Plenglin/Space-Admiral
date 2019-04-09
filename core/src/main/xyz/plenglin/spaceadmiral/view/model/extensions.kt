package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.net.game.io.s2c.initial.ClientInitialPayload


fun ClientInitialPayload.toClientModel(): GameStateCM {
    return GameStateCM()
}