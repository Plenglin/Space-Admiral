package xyz.plenglin.spaceadmiral.game.sector.event

import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SectorEventDTO
import java.io.Serializable

interface SectorEvent : Serializable {
    fun toDTO(): SectorEventDTO
}
