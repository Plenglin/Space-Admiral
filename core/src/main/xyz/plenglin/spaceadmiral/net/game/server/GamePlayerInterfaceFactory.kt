package xyz.plenglin.spaceadmiral.net.game.server

import xyz.plenglin.spaceadmiral.game.team.Team
import java.util.*


/**
 * How the server sees clients. Handles the initialization phase of the game.
 */
interface GamePlayerInterfaceFactory {
    val team: UUID
    fun createPlayerInterface(team: Team, server: GameServer): GamePlayerInterface
}