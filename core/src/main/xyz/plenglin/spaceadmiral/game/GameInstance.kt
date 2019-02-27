package xyz.plenglin.spaceadmiral.game

import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.util.KDTree2
import java.io.Serializable

class GameInstance : Serializable {
    val gameState = GameState()
    //val clock = AdjustableClock()
    //val loop = EventLoop(clock)

    var shipTree = KDTree2<Ship>()
    var projTree = KDTree2<Projectile>()

    fun update() {
        /*
        val shipList = ArrayList<Ship>()
        val projList = ArrayList<Projectile>()

        /*
        teams.forEach { team ->
            team.squads.forEach { squad ->
                squad.update()
                squad.ships.forEach {
                    it.updateInitial()
                    shipList.add(it)
                }
            }
            team.projectiles.forEach { proj ->
                proj.updateInitial()
                projList.add(proj)
            }
        }*/

        shipList.shuffle()
        shipList.forEach {
            shipTree.insert(it.transform.posGlobal, it)
        }*/
        gameState.ships.forEach { _, s ->

        }
    }

}