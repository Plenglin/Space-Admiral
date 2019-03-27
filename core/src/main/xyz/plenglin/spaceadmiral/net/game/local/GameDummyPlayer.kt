package xyz.plenglin.spaceadmiral.net.game.local

import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.net.game.server.GamePlayerInterface
import xyz.plenglin.spaceadmiral.net.game.server.GamePlayerInterfaceFactory
import xyz.plenglin.spaceadmiral.net.game.server.GameServer
import java.util.*

class GameDummyPlayer(override val team: UUID) : GamePlayerInterfaceFactory {

    private inner class PlayerInterface(override val team: Team) : GamePlayerInterface {
        override val connected: Boolean = true

        override fun sendGameState(gs: ByteArray) {

        }
    }

    override fun createPlayerInterface(team: Team, server: GameServer): GamePlayerInterface {
        return PlayerInterface(team)
    }

}
