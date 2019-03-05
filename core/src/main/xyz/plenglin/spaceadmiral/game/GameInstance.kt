package xyz.plenglin.spaceadmiral.game

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.util.KDTree2
import java.io.Serializable

class GameInstance : Serializable {
    val gameState = GameState()
    //val clock = AdjustableClock()
    //val loop = EventLoop(clock)
    var step: Long = 0L

    var shipTree = KDTree2<Ship>()
    var projTree = KDTree2<Projectile>()

    fun update() {
        logger.debug("update {}", step)
        shipTree.clear()
        gameState.ships.forEach { _, s ->
            s.update()
            shipTree.insert(s.transform.posGlobal, s)
        }
        gameState.projectiles.forEach { _, p ->
            p.update()
        }
        gameState.projectiles.forEach { _, p ->
            val capsule = p.getDetonationCapsule()
            val hit = shipTree.findInRect(p.getDetectionBoundingBox())
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
        step += 1
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(GameInstance::class.java)
    }
}