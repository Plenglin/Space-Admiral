package xyz.plenglin.spaceadmiral.net.game.client

import xyz.plenglin.spaceadmiral.net.game.io.s2c.initial.ClientInitialPayload


/**
 * How the client sees the server. Handles the initialization phase of the game.
 */
interface GameServerInterfaceFactory {
    /**
     * Get the initial payload. If it is not available, blocks until it is.
     */
    fun awaitInitialPayload(): ClientInitialPayload

    /**
     * Create the server interface.
     */
    fun createServerInterface(client: GameClient): GameServerInterface
}