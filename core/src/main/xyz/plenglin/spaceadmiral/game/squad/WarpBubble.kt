package xyz.plenglin.spaceadmiral.game.squad

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.lerp
import xyz.plenglin.spaceadmiral.util.toFloatVector
import java.io.Serializable
import java.util.*

data class WarpBubble constructor(
        val uuid: UUID,
        val squads: Set<Squad>,
        val startSector: IntVector2,
        val startTime: Long,
        val endSector: IntVector2,
        val endTime: Long) : Serializable {

    val delta: Vector2 = endSector.toFloatVector().sub(startSector.toFloatVector())

    fun getPos(t: Long) = lerp(
            ((t - startTime).toFloat() / (endTime - startTime)).coerceIn(0f, 1f),
            startSector.toFloatVector(),
            endSector.toFloatVector()
    )

    fun hasArrived(t: Long) = t >= endTime

}