package xyz.plenglin.spaceadmiral.view.model

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.*
import xyz.plenglin.spaceadmiral.game.TadarData
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.view.ui.GameUI

class GameStateCM {
    /**
     * The UI that this is bound to. If `null`, then there is no UI and this client model is an AI.
     */
    var ui: GameUI? = null

    internal val sectorMap = hashMapOf<IntVector2, SectorCM>()
    val sectors get() = sectorMap.values

    internal val teamMap = hashMapOf<TeamID, TeamCM>()
    val teams get() = teamMap.values

    val bubbles = hashMapOf<WarpBubbleID, WarpBubbleCM>()
    var time = 0L

    var tadar: TadarData = TadarData()

    var lastUpdateTime: Long = 0L

    operator fun get(sectorID: SectorID): SectorCM? {
        return sectorMap[sectorID]
    }

    operator fun get(teamID: TeamID): TeamCM? {
        return teamMap[teamID]
    }

    operator fun get(squadID: SquadID): SquadCM? {
        return teamMap[squadID.team]?.get(squadID.squad)
    }

    fun update(payload: ClientUpdatePayload) {
        val unmentionedSectors = sectorMap.keys.toHashSet()  // Copy the keys
        tadar = payload.tadar

        for (team in teams) {
            for (squad in team.squads) {
                squad.visible = false
            }
        }

        for (dtoSector in payload.sectors) {
            val pos = dtoSector.pos
            unmentionedSectors.remove(pos)
            val sector = sectorMap.getOrPut(pos) {
                logger.info("Sector at {} not recorded. Creating new sector", pos)
                SectorCM(pos, this)
            }
            sector.updateWith(dtoSector)

            for (dtoSquad in dtoSector.squads) {
                val squad = this[dtoSquad.uuid]!!
                squad.updateWith(dtoSquad)
                squad.sector = sector

                for (dtoShip in dtoSquad.ships) {
                    val ship = squad[dtoShip.uuid.ship]!!
                    ship.updateWith(dtoShip)
                }
            }
        }

        lastUpdateTime = System.currentTimeMillis()
        // Prune sectors
        //sectors.values.removeAll {  }
    }

    operator fun get(shipID: ShipID): ShipCM? {
        return teamMap[shipID.squad.team]?.get(shipID.squad.squad)?.get(shipID.ship)
    }

    private companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(GameStateCM::class.java)
    }
}