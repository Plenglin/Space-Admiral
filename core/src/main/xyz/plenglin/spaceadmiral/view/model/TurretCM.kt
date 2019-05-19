package xyz.plenglin.spaceadmiral.view.model

import xyz.plenglin.spaceadmiral.TurretID
import xyz.plenglin.spaceadmiral.util.Transform2D

class TurretCM(val uuid: TurretID, val ship: ShipCM, val transform: Transform2D) {
    override fun toString(): String {
        return "TurretCM($uuid)"
    }
}
