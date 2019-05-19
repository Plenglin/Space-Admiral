package xyz.plenglin.spaceadmiral.net.game.io.s2c.update.sectorevent

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.SquadID
import xyz.plenglin.spaceadmiral.WarpBubbleID
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SectorEventDTO
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SquadUDTO
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.view.model.SectorCM

class WarpBubbleBeginEventDTO(override val targetSector: IntVector2, val bubbleID: WarpBubbleID, val velocity: Vector2, val squads: List<SquadID>) : SectorEventDTO {
    override fun applyTo(sector: SectorCM) {
    }

}

class WarpBubbleEndEventDTO(override val targetSector: IntVector2, val squads: List<SquadUDTO>) : SectorEventDTO {
    override fun applyTo(sector: SectorCM) {

    }

}

