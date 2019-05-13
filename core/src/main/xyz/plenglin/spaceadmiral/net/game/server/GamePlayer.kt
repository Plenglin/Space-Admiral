package xyz.plenglin.spaceadmiral.net.game.server

import xyz.plenglin.spaceadmiral.game.Sector
import xyz.plenglin.spaceadmiral.game.team.Team

class GamePlayer(val iface: GamePlayerInterface) {
    val team: Team = iface.team

    val occupied: MutableSet<Sector> = mutableSetOf()
}