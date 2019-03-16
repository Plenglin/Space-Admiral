package xyz.plenglin.spaceadmiral.view.ui.selection

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.util.MinMaxRectangle
import xyz.plenglin.spaceadmiral.util.minMaxLimits

data class SelectionState(val start: Vector2, val end: Vector2 = start.cpy(), var dragged: Boolean = false) {

    fun getSelectionBox(): MinMaxRectangle {
        return minMaxLimits(start.x, end.x, start.y, end.y)
    }

}
