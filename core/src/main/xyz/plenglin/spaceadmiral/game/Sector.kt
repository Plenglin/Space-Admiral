package xyz.plenglin.spaceadmiral.game

import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.weapon.FiringEvent
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.KDTree2
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class Sector(val parent: GameState, val pos: IntVector2) : Serializable {

    val squads = HashMap<UUID, Squad>()
    val projectiles = HashMap<UUID, Projectile>()

    val firingEvents: MutableList<FiringEvent> = mutableListOf()

    @Transient
    var shipTree: KDTree2<Ship>? = null
    @Transient
    var projectileTree: KDTree2<Projectile>? = null

    fun updateTrees() {
        firingEvents.clear()
        val shipTree = KDTree2<Ship>()
        shipTree.clear()
        squads.values.forEach { squad ->
            squad.ships.forEach { ship ->
                shipTree.insert(ship.transform.posGlobal, ship)
            }
        }
        this.shipTree = shipTree

        val projectileTree = KDTree2<Projectile>()
        projectiles.forEach { (_, projectile) ->
            projectileTree.insert(projectile.pos, projectile)
        }
        this.projectileTree = projectileTree
    }

    override fun hashCode(): Int {
        return pos.hashCode()
    }
}
