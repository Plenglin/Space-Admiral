package xyz.plenglin.spaceadmiral.net.game.server

import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ObjectInputStream
import java.net.Socket

class TCPPlayerInterface(override val team: Team, private val socket: Socket) : GamePlayerInterface {
    override fun sendPayload(payload: ClientUpdatePayload) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val rx = BufferedInputStream(socket.getInputStream())
    private val rxObj = ObjectInputStream(rx)
    private val tx = BufferedOutputStream(socket.getOutputStream())
    //private val txObj = ObjectOutputStream(tx)

    override val connected: Boolean get() = socket.isConnected

}
