package xyz.plenglin.spaceadmiral.game

import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.weapon.FiringEvent
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.KDTree2
import java.util.*
import kotlin.collections.HashMap

class Sector(val parent: GameState, val pos: IntVector2) {

    @Transient
    private val ships = HashMap<UUID, Ship>()

    private val squads = HashMap<UUID, Squad>()
    private val projectiles = mutableListOf<Projectile>()

    val firingEvents: MutableList<FiringEvent> = mutableListOf()

    @Transient
    private var _shipTree: KDTree2<Ship>? = null
    val shipTree: KDTree2<Ship> get() {
        var obj = _shipTree
        if (obj == null) {
            obj = KDTree2()
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
            _projectileTree = obj
        }
        return obj
    }

    fun updateTrees() {
        shipTree.apply {
            clear()
            ships.forEach { (_, ship) ->
                insert(ship.transform.posGlobal, ship)
            }
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