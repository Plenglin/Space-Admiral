package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.net.client.SquadRef
import xyz.plenglin.spaceadmiral.util.Transform2D

sealed class CommandState {
    /**
     * The squads that will receive the command.
     */
    abstract val recipients: Set<SquadRef>
}

data class MoveToTransform(
        override val recipients: Set<SquadRef>,
        /**
         * The point in world space where the user began the drag.
         */
        val start: Vector2,
        /**
         * The point in world space where the user ended the drag, or is currently holding the mouse over.
         */
        var end: Vector2 = start,
        /**
         * Whether or not the user dragged during this command.
         */
        var dragged: Boolean = false) : CommandState() {

    fun generateSimpleTransform(): Map<SquadRef, SquadTransform> {
        val squadRefs = recipients.sortedBy { it()!!.index }
        val squadObjects = squadRefs.map { it()!! }
        val totalWidth: Float by lazy { recipients.map { it.getObject!!.transform.physicalWidth }.sum() }
        val centerOfMass = squadObjects
                .map { it.transform.transform.posGlobal.cpy() }
                .reduce { acc, vector2 -> acc.add(vector2) }
                .scl(1f / squadRefs.size)
        val displacement = end.cpy().sub(centerOfMass)
        val facing = displacement.angleRad()

        val step = displacement.cpy().rotate90(-1).nor()
        val pos = end.cpy().mulAdd(step, totalWidth / -2f)  // Start at the leftmost edge

        val outTransforms = ArrayList<SquadTransform>(squadRefs.size)
        for (i in squadRefs.indices) {
            val obj = squadObjects[i]
            val width = obj.transform.physicalWidth
            val center = pos.cpy().mulAdd(step, width / 2f)

            pos.mulAdd(step, width)
            outTransforms.add(obj.transform.copy(
                    transform = Transform2D(posLocal = center, angleLocal = facing)
            ))
        }

        return squadRefs.zip(outTransforms).toMap()
    }

    fun generateDraggedTransform(): Map<SquadRef, SquadTransform> {
        val squads = recipients.sortedBy { it()!!.index }
        val delta = end.cpy().sub(start)
        val perSquadDelta = delta.cpy().scl(0.5f / squads.size)
        val perSquadWidth = perSquadDelta.len()
        val facing: Float = delta.angleRad() + Math.PI.toFloat() / 2

        return squads.mapIndexed { index, squadRef ->
            val squad = squadRef()!!
            val template = squad.template
            val widthCount = (perSquadWidth / template.spacing).toInt()

            squadRef to SquadTransform(
                    Transform2D(start.cpy().mulAdd(perSquadDelta, index + 0.5f), facing),
                    widthCount,
                    template.spacing,
                    squad.ships.size
            )
        }.toMap()
    }
}

data class Attack(override val recipients: Set<SquadRef>, val target: SquadRef) : CommandState()
