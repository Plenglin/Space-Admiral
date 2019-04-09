package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ClientUpdatePayload
import xyz.plenglin.spaceadmiral.util.IntVector2
import java.util.*

class GameStateCM {
    val squads = hashMapOf<UUID, SquadCM>()
    val sectors = hashMapOf<IntVector2, SectorCM>()

    fun update(payload: ClientUpdatePayload) {
        val unmentionedSectors = sectors.keys.toHashSet()  // Copy the keys

        payload.sectors.forEach { dtoSector ->
            val pos = dtoSector.pos
            unmentionedSectors.remove(pos)
            val sector = sectors.getValue(pos)

            dtoSector.squads.forEach { dtoSquad ->
                val squad = squads.getValue(dtoSquad.uuid)
                squad.updateWith(dtoSquad)

                dtoSquad.ships.forEach { dtoShip ->
                    val ship = sector.ships.getValue(dtoShip.uuid)
                    ship.updateWith(dtoShip)
                }
            }
        }

        unmentionedSectors.forEach { sectors.remove(it) }
    }
}