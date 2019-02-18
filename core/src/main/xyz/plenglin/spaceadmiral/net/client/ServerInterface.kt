package xyz.plenglin.spaceadmiral.net.client

/**
 * How clients see the server.
 */
interface ServerInterface {
    val connected: Boolean

    fun attachClient(client: Client)
    fun sendDisconnectToServer()
}