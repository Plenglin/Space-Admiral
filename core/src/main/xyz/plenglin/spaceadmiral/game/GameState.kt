package xyz.plenglin.spaceadmiral.game

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.weapon.FiringEvent
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.util.KDTree2
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class GameState : Serializable {

    val teams = HashMap<UUID, Team>()
    val squads = HashMap<UUID, Squad>()
    val ships = HashMap<UUID, Ship>()
    val projectiles = HashMap<UUID, Projectile>()

    val firingEvents: MutableList<FiringEvent> = mutableListOf()

    @Transient
    private var _shipTree: KDTree2<Ship>? = null
    val shipTree: KDTree2<Ship> get() {
        var obj = _shipTree
        if (obj == null) {
            obj = KDTree2()
            val ship = ships.values.toMutableList()
            ship.shuffle()  // Jesus take the wheel and make us efficient
            ship.forEach {
                obj.insert(it.transform.posGlobal, it)
            }
            _shipTree = obj
        }
        return obj
    }

    @Transient
    private var _projectileTree: KDTree2<Projectile>? = null
    val projectileTree: KDTree2<Projectile> get() {
        var obj = _projectileTree
        if (obj == null) {
            obj = KDTree2()
            val ship = projectiles.values.toMutableList()
            ship.shuffle()  // Jesus take the wheel and make us efficient
            ship.forEach {
                obj.insert(it.pos, it)
            }
            _projectileTree = obj
        }
        return obj
    }

    var time: Long = 0L

    fun createTeam(color: Color, uuid: UUID = UUID.randomUUID()): Team {
        val out = Team(this, color, uuid = uuid)
        teams[out.uuid] = out
        return out
    }

    /*
    fun updateTrees() {
        shipTree = KDTree2()
        projectileTree = KDTree2()

        val ship = ships.values.toMutableList()
        ship.shuffle()  // Jesus take the wheel and make us efficient
        ship.forEach {
            shipTree.insert(it.transform.posGlobal, it)
        }

        val projectile = projectiles.values.toMutableList()
        projectile.shuffle()
        projectile.forEach {
            projectileTree.insert(it.pos, it)
        }
    }*/

}
