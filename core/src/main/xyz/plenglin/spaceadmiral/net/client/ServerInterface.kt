package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.net.io.ClientCommand
import java.util.*

/**
 * How clients see the server.
 */
interface ServerInterface {
    val connected: Boolean
    val clientTeam: UUID

    fun attachClient(client: GameClient)
    fun sendCommandToServer(command: ClientCommand)

    fun sendDisconnectToServer()
}
