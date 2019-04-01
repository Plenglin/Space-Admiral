package xyz.plenglin.spaceadmiral.net.game.client

import xyz.plenglin.spaceadmiral.net.game.io.ClientCommand
import java.util.*

/**
 * How clients see the server. Handles the update/main phase of the game.
 */
interface GameServerInterface {
    val connected: Boolean
    val team: UUID

    fun queueCommandToServer(command: ClientCommand)
    fun sendDisconnectToServer()
    fun commitCommandsToServer()
}
