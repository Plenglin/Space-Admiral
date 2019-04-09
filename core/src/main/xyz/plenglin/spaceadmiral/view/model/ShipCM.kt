package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.net.game.io.dto.ShipDTO
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.util.*

class ShipCM(val uuid: UUID, val transform: Transform2D) {

    fun updateWith(dto: ShipDTO) {
        transform.set(dto.transform)
    }

}