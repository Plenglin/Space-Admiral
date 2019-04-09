package xyz.plenglin.spaceadmiral.net.game.server

import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload

/**
 * How the server sees clients. Handles the update/main phase of the game.
 */
interface GamePlayerInterface {
    val connected: Boolean
    val team: Team

    fun sendPayload(payload: ClientUpdatePayload)

}