package xyz.plenglin.spaceadmiral.game.sector.event

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.sector.Sector
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SectorEventDTO
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.asUpdateDTO
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.sectorevent.WarpBubbleBeginEventDTO
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.sectorevent.WarpBubbleEndEventDTO
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.toFloatVector
import java.util.*

class WarpBubbleBeginEvent(val bubbleUUID: UUID, val from: Sector, val to: Sector, val squads: List<Squad>) : SectorEvent {

    val velocity: Vector2 = to.pos.toFloatVector().sub(from.pos.toFloatVector()).setLength(2f)

    override fun toDTO(gameState: GameState, target: IntVector2): SectorEventDTO {
        return WarpBubbleBeginEventDTO(target, bubbleUUID, velocity, squads.map { it.uuid })
    }

}

class WarpBubbleEndEvent(val bubbleUUID: UUID) : SectorEvent {
    override fun toDTO(gameState: GameState, target: IntVector2): SectorEventDTO {
        val bubble = gameState.warpBubbles[bubbleUUID]!!
        return WarpBubbleEndEventDTO(bubble.endSector, bubble.squads.map { it.asUpdateDTO() })
    }

}
