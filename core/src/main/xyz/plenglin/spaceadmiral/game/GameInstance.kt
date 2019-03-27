package xyz.plenglin.spaceadmiral.game

import org.slf4j.LoggerFactory
import java.io.Serializable

class GameInstance : Serializable {
    val gameState = GameState()
    //val clock = AdjustableClock()
    //val loop = EventLoop(clock)
    var step: Long = 0L

    fun update() {
        logger.debug("update {}", step)

        gameState.firingEvents.clear()
        gameState.updateTrees()

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

        step++
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(GameInstance::class.java)
    }
}