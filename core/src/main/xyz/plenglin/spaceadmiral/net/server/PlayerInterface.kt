package xyz.plenglin.spaceadmiral.net.server

import xyz.plenglin.spaceadmiral.game.team.Team

/**
 * How the server sees clients.
 */
interface PlayerInterface {
    val connected: Boolean
    var team: Team

    fun attachServer(server: Server)
    fun sendGameState()
}