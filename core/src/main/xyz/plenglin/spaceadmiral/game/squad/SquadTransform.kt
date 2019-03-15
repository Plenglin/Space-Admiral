package xyz.plenglin.spaceadmiral.game.squad

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.util.Transform2D

data class SquadTransform(val transform: Transform2D = Transform2D(), var width: Int, var spacing: Float, var count: Int) {
    val physicalWidth: Float get() = (width - 1) * spacing

    fun generateChildTransforms(): List<Transform2D> {
        val out = ArrayList<Transform2D>(count)
        val physicalWidth = physicalWidth
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

    fun set(target: SquadTransform) {
        transform.set(target.transform)
        width = target.width
        spacing = target.spacing
        count = target.count
    }

    companion object {
        @JvmStatic
        fun createP2P(left: Vector2, right: Vector2, spacing: Float, count: Int): SquadTransform {
            val pos = left.cpy().add(right).scl(0.5f)  // Midpoint of left and right
            val diff = left.cpy().sub(right)
            val width = (diff.len() / spacing).toInt()
            val facing = diff.rotate90(1)
            return SquadTransform(Transform2D(pos, facing.angleRad()), width, spacing, count)
        }
    }
}
