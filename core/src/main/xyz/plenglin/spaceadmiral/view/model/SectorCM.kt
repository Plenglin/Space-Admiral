package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.net.game.io.dto.SectorDTO
import java.util.*

class SectorCM {

    val ships = hashMapOf<UUID, ShipCM>()

    fun update(dto: SectorDTO) {
    }

}