package xyz.plenglin.spaceadmiral.game.squad

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.GameObject
import xyz.plenglin.spaceadmiral.game.GameStateTraverser
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.game.action.SquadAction
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.util.*
import kotlin.collections.ArrayList

class Squad(val template: ShipType, var team: Team) : GameObject {

    val ships: MutableList<Ship> = (1..template.squadSize).map { Ship(this) }.toMutableList()
    val actionQueue = LinkedList<SquadAction>()

    val transform = Transform2D(Vector2(), 0f)
    var formationWidth: Int = template.defaultFormationWidth

    init {
        resetShipPositions()
    }

    override fun acceptTraverser(traverser: GameStateTraverser) {
        traverser.traverse(this)
        ships.forEach {
            it.acceptTraverser(traverser)
        }
    }

    fun update() {
        if (actionQueue.peek()?.future == null) {
            nextAction()
        }
    }

    private fun nextAction() {
        if (actionQueue.isEmpty()) {
            return
        }
        val action = actionQueue.remove()
        action.future = team.gameInstance.loop.schedule(action.createCoroutine())
    }

    fun resetShipPositions() {
        generateRelativeTransforms().zip(ships).forEach { (trs, ship) ->
            trs.update()
            ship.transform.posLocal.set(trs.posGlobal)
            ship.transform.angleLocal = trs.angleGlobal
        }
    }

    fun generateRelativeTransforms(): List<Transform2D> {
        val out = ArrayList<Transform2D>(ships.size)
        val physicalWidth = (formationWidth - 1) * template.spacing
        val mainHeight = ships.size / formationWidth
        (0 until mainHeight).forEach { y ->
            (0 until formationWidth).forEach { x ->
                val trs = Transform2D(
                        Vector2(-y * template.spacing, x * template.spacing - physicalWidth / 2),
                        0f, transform
                )
                out.add(trs)
            }
        }
        val leftoverWidth = ships.size % formationWidth
        val physicalLeftoverWidth = (leftoverWidth - 1) * template.spacing
        val physicalMainHeight = mainHeight * (template.spacing - 1)
        val leftoverOffsetY = -physicalMainHeight - mainHeight * template.spacing
        (0 until leftoverWidth).forEach { x ->
            val trs = Transform2D(
                    Vector2(leftoverOffsetY, x * template.spacing - physicalLeftoverWidth / 2),
                    0f, transform
            )
            out.add(trs)
        }
        return out
    }

}