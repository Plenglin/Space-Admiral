package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.ShipID
import xyz.plenglin.spaceadmiral.SpaceAdmiral.Companion.DELTA_TIME
import xyz.plenglin.spaceadmiral.game.action.SquadAction
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.nextShipID
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.io.Serializable
import java.util.*

class Ship(val parent: Squad, val number: Int) : Serializable {

    val team get() = parent.team
    val gameState get() = team.gameState
    val template get() = parent.template
    val sector get() = parent.sector

    var transformIndex = number
    val uuid: ShipID = nextShipID()
    val transform = Transform2D(Vector2(), 0f)
    val target: Transform2D = transform

    val stateScheduler = StateScheduler()

    var health = template.health.copy()
    var morale = 0f
    var flags = 0

    val turrets = template.turrets.map { it.createMount(this) }
    val velocity = Vector2()

    init {

    }

    fun onDeath() {
        flags = flags or IS_DEAD
        logger.debug("{} died, set flags to {}", this, flags)
    }

    fun updateLogic() {
        val error = target.posGlobal.cpy().sub(transform.posGlobal)
        val delta = error.cpy().setLength(template.speed)
        velocity.set(delta)

        stateScheduler.update()
        turrets.forEach {
            it.update()
        }
    }

    fun updatePosition() {
        transform.posLocal.mulAdd(velocity, DELTA_TIME)
        if (velocity.len2() != 0f) {
            transform.angleLocal = velocity.angleRad()
        }
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(Ship::class.java)

        const val IS_DEAD = 0x1
    }
}

abstract class ShipAction(val parent: SquadAction, val ship: Ship) : Serializable, State
