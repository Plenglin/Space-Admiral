package xyz.plenglin.spaceadmiral.game.sector

import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SquadID
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.sector.event.SectorEvent
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.weapon.FiringEvent
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.squad.WarpBubble
import xyz.plenglin.spaceadmiral.nextWarpBubbleID
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.KDTree2
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class Sector(val parent: GameState, val pos: IntVector2) : Serializable {

    val squads = HashMap<SquadID, Squad>()
    val projectiles = HashMap<SquadID, Projectile>()

    val firingEvents: MutableList<FiringEvent> = mutableListOf()
    val recentlyDiedShips = mutableListOf<Ship>()
    val events: LinkedList<SectorEvent> = LinkedList()

    @Transient
    var shipTree: KDTree2<Ship>? = null
    @Transient
    var projectileTree: KDTree2<Projectile>? = null

    fun updateInitial() {
        // Clear events
        events.clear()
        firingEvents.clear()
        recentlyDiedShips.clear()

        // Generate ship tree
        val shipTree = KDTree2<Ship>()
        shipTree.clear()
        squads.values.forEach { squad ->
            squad.ships.forEach { ship ->
                shipTree.insert(ship.transform.posGlobal, ship)
            }
        }
        this.shipTree = shipTree

        // Generate projectile tree
        val projectileTree = KDTree2<Projectile>()
        projectiles.forEach { (_, projectile) ->
            projectileTree.insert(projectile.pos, projectile)
        }
        this.projectileTree = projectileTree
    }

    fun createWarpBubble(squads: Set<SquadID>, destination: Sector): WarpBubble {
        //if (this.squads.keys.containsAll(squads)) {
        //    throw IllegalArgumentException("Not all squads in $squads are in $this!")
        //}
        logger.info("Creating warp bubble from {} to {} with squads {}", this, destination, squads)
        val bubbledSquads = squads.mapNotNull(this.squads::get).toSet()
        squads.forEach {
            val squad = this.squads.remove(it)!!
            squad.sector = null
        }
        val out = WarpBubble(nextWarpBubbleID(), bubbledSquads, this.pos, parent.time, destination.pos, parent.time + 50)
        parent.warpBubbles[out.uuid] = out
        return out
    }

    override fun hashCode(): Int {
        return pos.hashCode()
    }

    override fun toString(): String {
        return "Sector($pos)"
    }

    fun insertSquad(squad: Squad, pos: Vector2) {
        squad.sector = this
        squads[squad.uuid] = squad
    }

    private companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(Sector::class.java)
    }
}
