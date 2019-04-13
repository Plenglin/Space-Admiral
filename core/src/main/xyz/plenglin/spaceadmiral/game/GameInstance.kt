package xyz.plenglin.spaceadmiral.game

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SpaceAdmiral.DELTA_TIME
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import java.io.Serializable

class GameInstance : Serializable {
    val gameState = GameState()
    //val clock = AdjustableClock()
    //val loop = EventLoop(clock)

    fun update() {
        logger.debug("update {}", gameState.time)

        gameState.firingEvents.clear()
        gameState.sectors.forEach { _, sector ->
            sector.updateTrees()
        }

        gameState.squads.forEach { _, squad ->
            squad.update()
        }

        val repulsors = mutableListOf<Ship>()
        gameState.ships.forEach { _, ship ->
            ship.updateLogic()
            if (ship.template.repulsion != null) {
                repulsors.add(ship)
            }
        }

        repulsors.forEach { ship ->
            println(ship.sector.shipTree!!)
            ship.sector.shipTree!!.findInCircle(ship.transform.posGlobal, ship.template.repulsion!!.range)
                    .map { it.value!! }
                    .filter { !ship.team.isAlliedWith(it.team) }
                    .forEach { other ->
                        val r2 = ship.transform.posGlobal.dst2(other.transform.posGlobal)
                        val mag = ship.template.repulsion!!.force / r2
                        val force = other.transform.posGlobal.cpy().sub(ship.transform.posGlobal).setLength(mag).scl(DELTA_TIME)

                        other.velocity.mulAdd(force, 1 / other.template.mass)
                        ship.velocity.mulAdd(force, -1 / ship.template.mass)
                    }
        }

        gameState.ships.forEach { _, ship ->
            ship.updatePosition()
        }

        /*gameState.projectiles.forEach { _, p ->
            val capsule = p.getDetonationCapsule()
            val hit = gameState.shipTree.findInRect(p.getDetectionBoundingBox())
                    .filter { p.canHit(it) }
                    .filter { capsule.contains(it.key) }
                    .map { it.value!! }
                    .toList()
            if (hit.isNotEmpty()) {
                hit.forEach {
                    p.onInteractWith(it)
                }
            }
        }*/

        val deadShips = mutableListOf<Ship>()
        gameState.ships.forEach { _, ship ->
            if (ship.health.isDead) {
                deadShips.add(ship)
                ship.onDeath()
            }
        }
        deadShips.forEach {
            it.parent.ships.remove(it)
            gameState.ships.remove(it.uuid)
            it.sector.onShipDeath(it)
        }
        val deadSquads = mutableListOf<Squad>()
        gameState.squads.forEach { _, squad ->
            if (squad.isDead) {
                deadSquads.add(squad)
                squad.onDeath()
            }
        }
        deadSquads.forEach {
            gameState.squads.remove(it.uuid)
        }
        gameState.time++
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(GameInstance::class.java)
    }
}