package xyz.plenglin.spaceadmiral.game

import org.slf4j.LoggerFactory
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
        //gameState.updateTrees()

        gameState.squads.forEach { _, squad ->
            squad.update()
        }

        gameState.ships.forEach { _, ship ->
            ship.update()
        }

        gameState.projectiles.forEach { _, p ->
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
        }

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