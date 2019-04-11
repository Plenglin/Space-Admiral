package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ShipUDTO
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.util.*

class ShipCM(val uuid: UUID, val squad: SquadCM, val transform: Transform2D) {
    val team: TeamCM get() = squad.team
    val template: ShipType get() = squad.template
    val turrets = hashMapOf<UUID, TurretCM>()

    fun updateWith(dto: ShipUDTO) {
        transform.set(dto.transform)
    }

}