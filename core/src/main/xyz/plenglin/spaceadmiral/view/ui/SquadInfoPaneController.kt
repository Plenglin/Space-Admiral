package xyz.plenglin.spaceadmiral.view.ui

import ktx.scene2d.label
import ktx.scene2d.table
import xyz.plenglin.spaceadmiral.view.model.SquadCM

class SquadInfoPaneController(val squad: SquadCM) {

    val action = SquadActionController(squad)

    val table = table {
        label(squad.displayName)
        row()
        //image(squad.icon)
    }

}