package xyz.plenglin.spaceadmiral.net.game.client

import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.net.game.io.ClientCommand
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class TCPServerInterface(val client: GameClient, override val team: UUID, private val socket: Socket) : GameServerInterface {

    private val rx = BufferedInputStream(socket.getInputStream())
    private val rxObj = ObjectInputStream(rx)
    private val tx = BufferedOutputStream(socket.getOutputStream())
    private val txObj = ObjectOutputStream(tx)

    private val rxThread = thread(start = true, isDaemon = true) {
        while (true) {
            val gs = rxObj.readObject()
            client.gameState = gs as GameState
        }
    }

    override fun commitCommandsToServer() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun queueCommandToServer(command: ClientCommand) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendDisconnectToServer() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val connected: Boolean get() = socket.isConnected


}