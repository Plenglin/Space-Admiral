package xyz.plenglin.spaceadmiral.net.game.client


/**
 * How the client sees the server. Handles the initialization phase of the game.
 */
interface GameServerInterfaceFactory {
    fun createServerInterface(client: GameClient): GameServerInterface
}