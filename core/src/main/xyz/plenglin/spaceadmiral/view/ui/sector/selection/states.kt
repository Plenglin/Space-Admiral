package xyz.plenglin.spaceadmiral.view.ui.sector.selection

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.KDTree2
import xyz.plenglin.spaceadmiral.util.MinMaxRectangle
import xyz.plenglin.spaceadmiral.util.minMaxLimits
import java.util.*

data class SelectionState(val team: UUID, val start: Vector2, val end: Vector2 = start.cpy(), var dragged: Boolean = false) {

    fun getSelectionBox(): MinMaxRectangle {
        return minMaxLimits(start.x, end.x, start.y, end.y)
    }

    fun getSelectedSquads(tree: KDTree2<Ship>): Set<Squad> {
        return tree.findInRect(getSelectionBox())
                .map { (_, s) -> s!!.parent }
                .filter { it.team.uuid == team }
                .toSet()
    }

}
