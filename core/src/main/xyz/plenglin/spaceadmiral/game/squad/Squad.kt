package xyz.plenglin.spaceadmiral.game.squad

import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.ShipID
import xyz.plenglin.spaceadmiral.SquadID
import xyz.plenglin.spaceadmiral.game.action.SquadAction
import xyz.plenglin.spaceadmiral.game.sector.Sector
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.util.StateScheduler
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.io.Serializable
import java.util.*

class Squad constructor(val template: ShipType, var team: Team, var sector: Sector?, val uuid: SquadID) : Serializable {
    val gameState get() = team.gameState
    val isDead: Boolean get() = ships.isEmpty()

    private var nextShipId: Short = 0
        get() {
            return field++
        }

    val ships: MutableList<Ship> = (0 until template.squadSize).map {
        Ship(this, it, ShipID(uuid, it.toShort()))
    }.toMutableList()
    val actionQueue: Queue<SquadAction> = LinkedList()

    val stateScheduler = StateScheduler()

    val transform = SquadTransform(
            count = template.squadSize,
            spacing = template.spacing,
            width = template.defaultFormationWidth)

    val centerOfMass: Vector2 get() {
        val out = Vector2(0f, 0f)
        ships.forEach {
            out.add(it.transform.posGlobal)
        }
        out.scl(1f / ships.size)
        return out
    }

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
            ship.target.set(ship.transform)
        }
    }

    fun onDeath() {


    }

    fun setTarget(transform: Transform2D) {
        this.transform.transform.setGlobal(transform)
    }

    override fun toString(): String {
        return "Squad($uuid)"
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(Squad::class.java)
    }

}
