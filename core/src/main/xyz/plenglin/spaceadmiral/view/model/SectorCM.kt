package xyz.plenglin.spaceadmiral.view.model

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.ShipID
import xyz.plenglin.spaceadmiral.SquadID
import xyz.plenglin.spaceadmiral.TurretID
import xyz.plenglin.spaceadmiral.game.ship.weapon.FiringEvent
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SectorUDTO
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.KDTree2

class SectorCM(val pos: IntVector2, val gameState: GameStateCM) {

    val squads = hashMapOf<SquadID, SquadCM>()
    val ships = hashMapOf<ShipID, ShipCM>()
    val shipTree = KDTree2<ShipCM>()
    val turrets = hashMapOf<TurretID, TurretCM>()

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
        logger.trace("Updating trees of {}", this)
        shipTree.clear()
        val ships = ships.values.toMutableList()//.let { ships ->
        ships.forEach {
            shipTree.insert(it.transform.posGlobal, it)
        }
    }

    override fun toString(): String {
        return "SectorCM($pos)"
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SectorCM::class.java)
    }

}