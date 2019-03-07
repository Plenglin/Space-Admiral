package xyz.plenglin.spaceadmiral.net.local

import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.client.GameClient
import xyz.plenglin.spaceadmiral.net.client.ServerInterface
import xyz.plenglin.spaceadmiral.net.server.PlayerInterface
import xyz.plenglin.spaceadmiral.net.server.Server

class LocalBridge : ServerInterface, PlayerInterface {
    override lateinit var team: Team
    lateinit var client: GameClient
    lateinit var server: Server
    override val connected: Boolean = true

    override fun sendGameState(gs: GameState) {
        client.gameState = gs
    }

    override fun attachClient(client: GameClient) {
        this.client = client
    }

    override fun attachServer(server: Server) {
        this.server = server
    }

    override fun sendDisconnectToServer() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}