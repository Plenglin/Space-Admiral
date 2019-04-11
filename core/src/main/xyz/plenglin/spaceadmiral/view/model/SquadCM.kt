package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SquadUDTO
import java.util.*
import kotlin.collections.HashMap

class SquadCM(val uuid: UUID, val team: TeamCM, val template: ShipType, val transform: SquadTransform) {

    var sector: SectorCM? = null
    val ships: MutableMap<UUID, ShipCM> = HashMap()
    var index: Int = 0
    var visible = false

    fun updateWith(dto: SquadUDTO) {
        visible = true
    }

}
