package xyz.plenglin.spaceadmiral.net.local

import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.client.GameClient
import xyz.plenglin.spaceadmiral.net.client.ServerInterface
import xyz.plenglin.spaceadmiral.net.io.Command
import xyz.plenglin.spaceadmiral.net.server.PlayerInterface
import xyz.plenglin.spaceadmiral.net.server.Server
import java.util.*

class LocalBridge : ServerInterface, PlayerInterface {
    lateinit var client: GameClient
    lateinit var server: Server
    override val connected: Boolean = true

    override lateinit var clientTeam: UUID
        private set

    private var _team: Team? = null
    override var team: Team
        get() = _team!!
        set(value) {
            _team = value
            clientTeam = value.uuid
        }

    override fun sendGameState(gs: GameState) {
        client.gameState = gs
    }

    override fun attachClient(client: GameClient) {
        this.client = client
    }

    override fun attachServer(server: Server) {
        this.server = server
    }

    override fun sendCommandToServer(command: Command) {
        server.onCommandReceived(this, command)
    }

    override fun sendDisconnectToServer() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}