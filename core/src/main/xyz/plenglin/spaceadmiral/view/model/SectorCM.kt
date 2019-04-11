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

    private var treesDirty = true

    fun updateWith(dto: SectorUDTO) {
        treesDirty = true
    }

    fun updateTrees() {
        logger.debug("Updating trees.")
        shipTree.clear()
        ships.values.toMutableList().let { ships ->
            logger.debug("Adding ships: {}", ships)
            ships.shuffle()
            ships.forEach {
                shipTree.insert(it.transform.posGlobal, it)
            }
        }
        treesDirty = false
    }

    fun updateTreesIfNecessary() {
        if (treesDirty) {
            updateTrees()
        }
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SectorCM::class.java)
    }

}