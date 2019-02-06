package xyz.plenglin.spaceadmiral.game.squad

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

    val ships: MutableList<Ship>
    val actionQueue = LinkedList<SquadAction>()

    val transform = Transform2D(Vector2(), 0f)
    var formationWidth: Int = template.defaultFormationWidth

    init {
        ships = generateRelativeTransforms().map { trs ->
            return@map Ship(this).apply {
                transform.set(trs)
            }
        }.toMutableList()
    }

    override fun acceptTraverser(traverser: GameStateTraverser) {
        traverser.traverse(this)
        ships.forEach {
            it.acceptTraverser(traverser)
        }
    }

    fun update() {
        if (actionQueue.peek().future == null) {
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

    fun generateRelativeTransforms(): List<Transform2D> {
        val out = ArrayList<Transform2D>(ships.size)
        val physicalWidth = (formationWidth - 1) * template.spacing
        val mainFormationHeight = ships.size % formationWidth - 1
        val physicalHeight = mainFormationHeight * template.spacing
        (0 until formationWidth).forEach { x ->
            (0 until mainFormationHeight).forEach { y ->
                val trs = Transform2D(
                        Vector2(x * template.spacing - physicalWidth / 2, y * template.spacing - physicalHeight / 2),
                        0f
                )
                out.add(trs)
            }
        }
        return out
    }
}