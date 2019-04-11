package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.game.TadarData
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.util.IntVector2
import java.util.*

class GameStateCM {
    val sectors = hashMapOf<IntVector2, SectorCM>()

    val teams = hashMapOf<UUID, TeamCM>()
    val squads = hashMapOf<UUID, SquadCM>()
    val ships = hashMapOf<UUID, ShipCM>()

    var tadar: TadarData = TadarData()

    var lastUpdateTime: Long = 0L

    fun update(payload: ClientUpdatePayload) {
        val unmentionedSectors = sectors.keys.toHashSet()  // Copy the keys
        tadar = payload.tadar

        squads.values.forEach {
            it.visible = false
        }

        payload.sectors.forEach { dtoSector ->
            val pos = dtoSector.pos
            unmentionedSectors.remove(pos)
            val sector = sectors.getOrPut(pos) { SectorCM(pos, this) }
            sector.updateWith(dtoSector)

            dtoSector.squads.forEach { dtoSquad ->
                val squad = squads.getValue(dtoSquad.uuid)
                squad.updateWith(dtoSquad)

                dtoSquad.ships.forEach { dtoShip ->
                    val ship = ships.getValue(dtoShip.uuid)
                    ship.updateWith(dtoShip)
                }
            }
        }

        lastUpdateTime = System.currentTimeMillis()
        // Prune sectors
        //sectors.values.removeAll {  }
    }

}