package xyz.plenglin.spaceadmiral.net.game.server

import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.team.Team

/**
 * How the server sees clients. Handles the update/main phase of the game.
 */
interface GamePlayerInterface {
    val connected: Boolean
    val team: Team

    fun sendGameState(gs: GameState)

}