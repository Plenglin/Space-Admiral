package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.server.PlayerInterface
import xyz.plenglin.spaceadmiral.net.server.Server

class LocalBridge : ServerInterface, PlayerInterface {
    override lateinit var team: Team
    lateinit var client: Client
    lateinit var server: Server
    override val connected: Boolean = true

    override fun sendGameState() {
        client.gameState.teams.apply {
            clear()
            addAll(server.instance.teams)
        }
    }

    override fun attachClient(client: Client) {
        this.client = client
    }
    override fun attachServer(server: Server) {
        this.server = server
    }



}