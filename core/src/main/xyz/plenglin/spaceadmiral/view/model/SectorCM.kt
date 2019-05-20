package xyz.plenglin.spaceadmiral.view.model

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.TurretID
import xyz.plenglin.spaceadmiral.game.ship.weapon.FiringEvent
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SectorUDTO
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.KDTree2

class SectorCM(val pos: IntVector2, val gameState: GameStateCM) {

    val squads = mutableListOf<SquadCM>()

    val shipTree = KDTree2<ShipCM>()
    val turrets = hashMapOf<TurretID, TurretCM>()

    val projectileTree = KDTree2<ProjectileCM>()
    val firingEvents = mutableListOf<FiringEvent>()

    fun updateWith(dto: SectorUDTO) {
        logger.trace("Updating rendering data of {} with {}", this, dto)
        squads.clear()
        shipTree.clear()
        for (squad in dto.squads) {
            squads.add(gameState[squad.uuid]!!)
        }
        firingEvents.clear()
        firingEvents.addAll(dto.firingEvents)
        dto.recentlyDiedShips.forEach {
            gameState[it]!!.onDead()
        }
    }

    fun onRender() {
        logger.trace("Updating rendering data of {}", this)
        shipTree.clear()
        squads.clear()

        for (squad in squads) {
            if (squad.sector != this) continue

            squads.add(squad)
            for (ship in squad.ships) {
                shipTree.insert(ship.transform.posGlobal, ship)
            }
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