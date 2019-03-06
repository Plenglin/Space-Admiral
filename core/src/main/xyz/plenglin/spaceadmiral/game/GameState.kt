package xyz.plenglin.spaceadmiral.game

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.util.KDTree2
import java.util.*
import kotlin.collections.HashMap

class GameState {

    val teams = HashMap<UUID, Team>()
    val squads = HashMap<UUID, Squad>()
    val ships = HashMap<UUID, Ship>()
    val projectiles = HashMap<UUID, Projectile>()

    @Transient val shipTree = KDTree2<Ship>()
    @Transient val projectileTree = KDTree2<Projectile>()

    fun createTeam(color: Color): Team {
        val out = Team(this, color)
        teams[out.uuid] = out
        return out
    }

    fun updateTrees() {
        shipTree.clear()
        val ship = ships.values.toMutableList()
        ship.shuffle()
        ship.forEach {
            shipTree.insert(it.transform.posGlobal, it)
        }

        projectileTree.clear()
        val projectile = projectiles.values.toMutableList()
        projectile.shuffle()
        projectile.forEach {
            projectileTree.insert(it.pos, it)
        }
    }

}
