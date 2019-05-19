package xyz.plenglin.spaceadmiral.net.game.local

import xyz.plenglin.spaceadmiral.TeamID
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.net.game.server.GamePlayerInterface
import xyz.plenglin.spaceadmiral.net.game.server.GamePlayerInterfaceFactory
import xyz.plenglin.spaceadmiral.net.game.server.GameServer
import java.util.*

class GameDummyPlayer(override val team: TeamID) : GamePlayerInterfaceFactory {

    private inner class PlayerInterface(override val team: Team) : GamePlayerInterface {
        override fun sendPayload(payload: ClientUpdatePayload) {
        }

        override val connected: Boolean = true

    }

    override fun createPlayerInterface(team: Team, server: GameServer): GamePlayerInterface {
        return PlayerInterface(team)
    }

}
