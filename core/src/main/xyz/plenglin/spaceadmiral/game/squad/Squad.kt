package xyz.plenglin.spaceadmiral.game.squad

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.util.StateScheduler
import java.io.Serializable
import java.util.*

class Squad(val template: ShipType, var team: Team, var index: Int) : Serializable {
    val ships: MutableList<Ship> = (0 until template.squadSize).map { Ship(this, it) }.toMutableList()
    val actionQueue: Queue<SquadAction> = LinkedList()

    val stateScheduler = StateScheduler()

    val transform = SquadTransform(
            count = template.squadSize,
            spacing = template.spacing,
            width = template.defaultFormationWidth)
    val uuid: UUID = UUID.randomUUID()

    init {
        resetShipPositions()
    }

    fun update() {
        if (stateScheduler.update()) {
            logger.debug("{} statescheduler ended something", this)
        }
        if (stateScheduler.currentState == null && actionQueue.peek() != null) {
            val nextState = actionQueue.remove()
            stateScheduler.nextState = nextState

            logger.info("{} will now start doing {}", this, nextState)
        }
    }

    fun resetShipPositions() {
        transform.generateChildTransforms().zip(ships).forEach { (trs, ship) ->
            trs.update()
            ship.transform.posLocal.set(trs.posGlobal)
            ship.transform.angleLocal = trs.angleGlobal
        }
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(Squad::class.java)
    }

}
