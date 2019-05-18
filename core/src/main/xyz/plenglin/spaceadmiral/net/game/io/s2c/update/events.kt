package xyz.plenglin.spaceadmiral.net.game.io.s2c.update

import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import java.io.Serializable

interface SectorEventDTO : Serializable {
    val targetSector: IntVector2
    fun applyTo(sector: SectorCM)
}

/*
data class WarpBubbleBeginEvent(val bubbleUUID: UUID, val pos: Vector2, val velocity: Vector2, val squads: List<UUID>) : SectorEventDTO {
    override fun applyTo(gameState: GameStateCM) {
        gameState.bubbles[bubbleUUID] = WarpBubbleCM(pos, velocity, gameState.time)
    }
}


data class WarpBubbleEndEvent(
        val bubbleUUID: UUID,
        /**
         * Where the bubble finished. Null if it went off TADAR.
         */
        val endSector: IntVector2?) : SectorEventDTO {
    override fun applyTo(gameState: GameStateCM) {
        gameState.bubbles[bubbleUUID]!!.lastSeen = gameState.time
        if (endSector != null) {
            gameState.bubbles.remove(bubbleUUID)
        }
    }
}
*/