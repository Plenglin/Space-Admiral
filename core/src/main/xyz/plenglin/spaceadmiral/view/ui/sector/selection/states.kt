package xyz.plenglin.spaceadmiral.view.ui.sector.selection

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.util.KDTree2
import xyz.plenglin.spaceadmiral.util.MinMaxRectangle
import xyz.plenglin.spaceadmiral.util.minMaxLimits
import xyz.plenglin.spaceadmiral.view.model.ShipCM
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.model.TeamCM

data class SelectionState(val team: TeamCM, val start: Vector2, val end: Vector2 = start.cpy(), var dragged: Boolean = false) {

    fun getSelectionBox(): MinMaxRectangle {
        return minMaxLimits(start.x, end.x, start.y, end.y)
    }

    fun getSelectedSquads(tree: KDTree2<ShipCM>): Set<SquadCM> {
        return tree.findInRect(getSelectionBox())
                .map { (_, ship) -> ship!!.squad }
                .filter { team.controls(it) }
                .toSet()
    }

}
