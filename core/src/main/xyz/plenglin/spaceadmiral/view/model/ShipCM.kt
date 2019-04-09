package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ShipDTO
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.util.*

class ShipCM(val uuid: UUID, val squad: SquadCM, val transform: Transform2D) {

    fun updateWith(dto: ShipDTO) {
        transform.set(dto.transform)
    }

}