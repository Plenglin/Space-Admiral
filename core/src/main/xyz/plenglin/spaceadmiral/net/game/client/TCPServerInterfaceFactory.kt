package xyz.plenglin.spaceadmiral.net.game.client

import java.net.Socket
import java.net.SocketAddress
import java.util.*


class TCPServerInterfaceFactory(val addr: SocketAddress) : GameServerInterfaceFactory {

    private val socket = Socket()

    override fun createServerInterface(client: GameClient): GameServerInterface {
        socket.connect(addr)
        val bytes = ByteArray(16)
        socket.getInputStream()
        return TCPServerInterface(client, UUID.randomUUID(), socket)
    }

}
