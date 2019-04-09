package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.game.ship.weapon.FiringEvent
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SectorUDTO
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.KDTree2
import java.util.*

class SectorCM(val pos: IntVector2, val gameState: GameStateCM) {

    val ships = hashMapOf<UUID, ShipCM>()
    val shipTree = KDTree2<ShipCM>()
    val projectileTree = KDTree2<ProjectileCM>()
    val firingEvents = mutableListOf<FiringEvent>()

    fun update(dto: SectorUDTO) {
    }

}