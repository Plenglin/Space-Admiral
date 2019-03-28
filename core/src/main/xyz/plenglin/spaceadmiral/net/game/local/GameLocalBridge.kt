package xyz.plenglin.spaceadmiral.net.game.local

import org.apache.commons.lang3.SerializationUtils
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.net.game.client.GameServerInterface
import xyz.plenglin.spaceadmiral.net.game.client.GameServerInterfaceFactory
import xyz.plenglin.spaceadmiral.net.game.io.ClientCommand
import xyz.plenglin.spaceadmiral.net.game.server.GamePlayerInterface
import xyz.plenglin.spaceadmiral.net.game.server.GamePlayerInterfaceFactory
import xyz.plenglin.spaceadmiral.net.game.server.GameServer
import java.util.*
import java.util.concurrent.locks.ReentrantLock

class GameLocalBridge(override val team: UUID) : GamePlayerInterfaceFactory, GameServerInterfaceFactory {

    private val lock = ReentrantLock()

    private var clientSide: ClientSide? = null
    private var serverSide: ServerSide? = null

    private inner class ServerSide(override val team: Team, val server: GameServer) : GamePlayerInterface {
        override val connected: Boolean get() = clientSide != null

        override fun sendGameState(gs: ByteArray) {
            clientSide?.let {
                it.client.gameState = SerializationUtils.deserialize(gs)
            }
        }

    }

    private inner class ClientSide(val client: GameClient) : GameServerInterface {
        override val connected: Boolean get() = serverSide != null
        override val team: UUID = this@GameLocalBridge.team

        override fun sendCommandToServer(command: ClientCommand) {
            serverSide?.let {
                it.server.onCommandReceived(it, command)
            }
        }

        override fun sendDisconnectToServer() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


    }

    override fun createPlayerInterface(team: Team, server: GameServer): GamePlayerInterface {
        val obj = ServerSide(team, server)
        serverSide = obj
        return obj
    }

    override fun createServerInterface(client: GameClient): GameServerInterface {
        val obj = ClientSide(client)
        clientSide = obj
        return obj
    }

}