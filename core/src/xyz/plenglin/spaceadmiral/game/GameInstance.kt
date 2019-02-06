package xyz.plenglin.spaceadmiral.game

import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.util.AdjustableClock
import xyz.plenglin.spaceadmiral.util.EventLoop

class GameInstance {
    val teams = mutableListOf<Team>()
    val clock = AdjustableClock()
    val loop = EventLoop(clock)

    fun update() {
        teams.forEach { team ->
            team.squads.forEach { squad ->
                squad.update()
                squad.ships.forEach {
                    it.update()
                }
            }
        }
    }
}