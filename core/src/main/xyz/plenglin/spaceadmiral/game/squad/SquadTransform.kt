package xyz.plenglin.spaceadmiral.game.squad

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.util.Transform2D

data class SquadTransform(val transform: Transform2D = Transform2D(), var width: Int, var spacing: Float, var count: Int) {
    fun generateChildTransforms(): List<Transform2D> {
        val out = ArrayList<Transform2D>(count)
        val physicalWidth = (width - 1) * spacing
        val mainHeight = count / width
        (0 until mainHeight).forEach { y ->
            (0 until width).forEach { x ->
                val trs = Transform2D(
                        Vector2(-y * spacing, x * spacing - physicalWidth / 2),
                        0f, transform
                )
                out.add(trs)
            }
        }
        val leftoverWidth = count % width
        val physicalLeftoverWidth = (leftoverWidth - 1) * spacing
        val physicalMainHeight = mainHeight * (spacing - 1)
        val leftoverOffsetY = -physicalMainHeight - mainHeight * spacing
        (0 until leftoverWidth).forEach { x ->
            val trs = Transform2D(
                    Vector2(leftoverOffsetY, x * spacing - physicalLeftoverWidth / 2),
                    0f, transform
            )
            out.add(trs)
        }
        return out
    }
}