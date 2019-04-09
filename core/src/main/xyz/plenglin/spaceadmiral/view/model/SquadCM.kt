package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SquadDTO
import java.util.*
import kotlin.collections.HashMap

class SquadCM(val uuid: UUID, val team: TeamCM, val template: ShipType) {

    val ships: HashMap<UUID, ShipCM> = HashMap()

    fun updateWith(dto: SquadDTO) {

    }

}
