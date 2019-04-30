package xyz.plenglin.spaceadmiral.view.ui

import ktx.scene2d.table
import xyz.plenglin.spaceadmiral.view.model.SquadCM

class SquadActionController(val squad: SquadCM) {

    val actionsTable = table {
        squad
    }

}