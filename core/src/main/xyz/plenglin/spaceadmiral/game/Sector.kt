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

    @Transient
    private var ships = HashMap<UUID, Ship>()

    val squads = HashMap<UUID, Squad>()
    private val projectiles = mutableListOf<Projectile>()

    val firingEvents: MutableList<FiringEvent> = mutableListOf()

    @Transient
    var shipTree: KDTree2<Ship>? = null
    @Transient
    var projectileTree: KDTree2<Projectile>? = null

    fun updateTrees() {
        var shipTree = shipTree
        if (shipTree == null) {
            shipTree = KDTree2()
            this.shipTree = shipTree
        }
        println("asdf")
        shipTree.clear()
        println("asdf")
        ships.forEach { (_, ship) ->
            shipTree.insert(ship.transform.posGlobal, ship)
        }
        println("asdf")

        var projectileTree = projectileTree
        if (projectileTree == null) {
            projectileTree = KDTree2()
            this.projectileTree = projectileTree
        }
        projectileTree.apply {
            projectiles.shuffle()
            clear()
            projectiles.forEach {
                insert(it.pos, it)
            }
        }
    }

    fun onSquadEnter(squad: Squad) {
        squad.ships.forEach {
            ships[it.uuid] = it
        }
        squads[squad.uuid] = squad
        squad.sector = this
    }

    fun onShipDeath(ship: Ship) {
        ships.remove(ship.uuid)
    }

    fun onSquadLeave(squad: Squad) {
        squad.ships.forEach {
            ships.remove(it.uuid)
        }
        squads.remove(squad.uuid)
    }
    
}
