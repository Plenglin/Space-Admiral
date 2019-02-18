package xyz.plenglin.spaceadmiral.net.server

import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.data.Delta
import xyz.plenglin.spaceadmiral.net.data.GameStateDTO

/**
 * How the server sees clients.
 */
interface PlayerInterface {
    val connected: Boolean
    var team: Team

    fun attachServer(server: Server)
    fun sendInitialGameState(initial: GameStateDTO)
    fun sendDeltas(deltas: List<Delta>)

}