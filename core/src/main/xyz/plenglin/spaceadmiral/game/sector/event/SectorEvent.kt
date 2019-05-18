package xyz.plenglin.spaceadmiral.game.sector.event

import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SectorEventDTO
import xyz.plenglin.spaceadmiral.util.IntVector2
import java.io.Serializable

interface SectorEvent : Serializable {
    fun toDTO(gameState: GameState, target: IntVector2): SectorEventDTO
}
