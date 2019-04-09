package xyz.plenglin.spaceadmiral.net.game.client

import xyz.plenglin.spaceadmiral.net.game.io.s2c.initial.ClientInitialPayload


/**
 * How the client sees the server. Handles the initialization phase of the game.
 */
interface GameServerInterfaceFactory {
    val initialPayload: ClientInitialPayload
    fun createServerInterface(client: GameClient): GameServerInterface
}