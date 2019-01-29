package xyz.plenglin.spaceadmiral.game

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.action.SquadAction
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.util.*
import kotlin.collections.ArrayList

class Squad(val template: ShipType, var team: Team) {
    val ships = mutableSetOf<Ship>()
    val actionQueue = LinkedList<SquadAction>()

    val transform = Transform2D(Vector2(), 0f)

    var formationWidth: Int = 0

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