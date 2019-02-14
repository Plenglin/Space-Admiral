package xyz.plenglin.spaceadmiral.game

import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.util.AdjustableClock
import xyz.plenglin.spaceadmiral.util.EventLoop
import xyz.plenglin.spaceadmiral.util.KDTree2
import xyz.plenglin.spaceadmiral.util.KDTree2Node

class GameInstance {
    val teams = mutableListOf<Team>()
    val clock = AdjustableClock()
    val loop = EventLoop(clock)

    val shipTree = KDTree2<Ship>()
    val projTree = KDTree2<Projectile>()

    fun update() {
        val shipList = ArrayList<Ship>()
        val projList = ArrayList<Projectile>()

        teams.forEach { team ->
            team.squads.forEach { squad ->
                squad.update()
                squad.ships.forEach {
                    it.update()
                    shipList.add(it)
                }
            }
            team.projectiles.forEach { proj ->
                proj.update()
                projList.add(proj)
            }
        }

        shipList.shuffle()
        shipList.forEach {
            shipTree.insert(it.transform.posGlobal, it)
        }

        projList.shuffle()
        projList.forEach {
            projTree.insert(it.pos, it)
        }

        for (p in projList) {
            //if (shipTree.find
        }
    }
}