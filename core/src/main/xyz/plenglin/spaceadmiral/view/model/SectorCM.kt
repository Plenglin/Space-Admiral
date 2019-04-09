package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SectorDTO
import xyz.plenglin.spaceadmiral.util.IntVector2
import java.util.*

class SectorCM(val pos: IntVector2, val gameState: GameStateCM) {

    val ships = hashMapOf<UUID, ShipCM>()

    fun update(dto: SectorDTO) {
    }

}