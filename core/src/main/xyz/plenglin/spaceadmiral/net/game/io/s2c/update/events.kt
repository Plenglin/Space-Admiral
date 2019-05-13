package xyz.plenglin.spaceadmiral.net.game.io.s2c.update

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.view.model.GameStateCM
import xyz.plenglin.spaceadmiral.view.model.WarpBubbleCM
import java.util.*

interface GameEvent {
    fun update(gameState: GameStateCM)
}

data class WarpBubbleBeginEvent(val bubbleUUID: UUID, val pos: Vector2, val velocity: Vector2, val squads: List<UUID>) : GameEvent {
    override fun update(gameState: GameStateCM) {
        gameState.bubbles[bubbleUUID] = WarpBubbleCM(pos, velocity, gameState.time)
    }
}


data class WarpBubbleEndEvent(
        val bubbleUUID: UUID,
        /**
         * Where the bubble finished. Null if it went off TADAR.
         */
        val endSector: IntVector2?) : GameEvent {
    override fun update(gameState: GameStateCM) {
        gameState.bubbles[bubbleUUID]!!.lastSeen = gameState.time
        if (endSector != null) {
            gameState.bubbles.remove(bubbleUUID)
        }
    }
}
