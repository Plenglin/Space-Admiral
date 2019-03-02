package xyz.plenglin.spaceadmiral.game.squad

import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.game.team.Team
import java.io.Serializable
import java.util.*

class Squad(val template: ShipType, var team: Team) : Serializable {

    val ships: MutableList<Ship> = (0 until template.squadSize).map { Ship(this, it) }.toMutableList()
    val actionQueue = LinkedList<SquadAction>()

    val transform = SquadTransform(
            count = template.squadSize,
            spacing = template.spacing,
            width = template.defaultFormationWidth)
    val uuid: UUID = UUID.randomUUID()

    init {
        resetShipPositions()
    }

    fun update() {
    }

    private fun nextAction() {
        if (actionQueue.isEmpty()) {
            return
        }
        val action = actionQueue.remove()
        //action.future = team.gameInstance.loop.schedule(action.createCoroutine())
    }

    fun resetShipPositions() {
        transform.generateChildTransforms().zip(ships).forEach { (trs, ship) ->
            trs.update()
            ship.transform.posLocal.set(trs.posGlobal)
            ship.transform.angleLocal = trs.angleGlobal
        }
    }

}
