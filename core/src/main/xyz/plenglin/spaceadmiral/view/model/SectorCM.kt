package xyz.plenglin.spaceadmiral.view.model

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.ship.weapon.FiringEvent
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SectorUDTO
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.KDTree2
import java.util.*

class SectorCM(val pos: IntVector2, val gameState: GameStateCM) {

    val squads = hashMapOf<UUID, SquadCM>()
    val ships = hashMapOf<UUID, ShipCM>()
    val shipTree = KDTree2<ShipCM>()
    val turrets = hashMapOf<UUID, TurretCM>()

    val projectileTree = KDTree2<ProjectileCM>()
    val firingEvents = mutableListOf<FiringEvent>()

    fun updateWith(dto: SectorUDTO) {
        firingEvents.clear()
        firingEvents.addAll(dto.firingEvents)
        dto.recentlyDiedShips.forEach {
            ships.getValue(it).onDead()
        }
    }

    fun updateTrees() {
        logger.debug("Updating trees of {}", this)
        shipTree.clear()
        val ships = ships.values.toMutableList()//.let { ships ->
        ships.shuffle()
        ships.forEach {
            shipTree.insert(it.transform.posGlobal, it)
        }
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SectorCM::class.java)
    }

}