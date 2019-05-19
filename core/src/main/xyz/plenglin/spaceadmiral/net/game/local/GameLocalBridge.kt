package xyz.plenglin.spaceadmiral.net.game.local

import xyz.plenglin.spaceadmiral.TeamID
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.net.game.client.GameServerInterface
import xyz.plenglin.spaceadmiral.net.game.client.GameServerInterfaceFactory
import xyz.plenglin.spaceadmiral.net.game.io.c2s.ClientCommand
import xyz.plenglin.spaceadmiral.net.game.io.s2c.initial.ClientInitialPayload
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.net.game.server.GamePlayerInterface
import xyz.plenglin.spaceadmiral.net.game.server.GamePlayerInterfaceFactory
import xyz.plenglin.spaceadmiral.net.game.server.GameServer
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock


class GameLocalBridge(override val team: TeamID) : GamePlayerInterfaceFactory, GameServerInterfaceFactory {
    var initialPayload: ClientInitialPayload? = null
    val lockServerSide = ReentrantLock()

    private var clientSide: ClientSide? = null
    private var serverSide: ServerSide? = null

    init {
        lockServerSide.lock()
    }

    private inner class ServerSide(override val team: Team, val server: GameServer) : GamePlayerInterface {
        override val connected: Boolean get() = clientSide != null

        override fun sendPayload(payload: ClientUpdatePayload) {
            clientSide?.client?.onReceivePayload(payload)
        }

    }

    private inner class ClientSide(val client: GameClient) : GameServerInterface {
        override val connected: Boolean get() = serverSide != null
        override val team: TeamID = this@GameLocalBridge.team

        private val commands = mutableListOf<ClientCommand>()

        override fun sendDisconnectToServer() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }


        override fun queueCommandToServer(command: ClientCommand) {
            commands.add(command)
        }

        override fun commitCommandsToServer() {
            val server = serverSide ?: return
            commands.forEach {
                server.server.onCommandReceived(server, it)
            }
            commands.clear()
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

    override fun awaitInitialPayload(): ClientInitialPayload {
        lockServerSide.withLock {
            return initialPayload!!
        }
    }

}
