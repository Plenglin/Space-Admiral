package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SpaceAdmiral.DELTA_TIME
import xyz.plenglin.spaceadmiral.game.action.SquadAction
import xyz.plenglin.spaceadmiral.game.squad.Squad
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
    val uuid: UUID = UUID.randomUUID()
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
        flags = flags or DIED_RECENTLY or IS_DEAD
        logger.debug("{} died, set flags to {}", this, flags)
    }

    fun updateLogic() {
        flags = flags and DIED_RECENTLY.inv()

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

    fun shouldRemove(): Boolean {
        return (flags and IS_DEAD != 0) && (flags and DIED_RECENTLY == 0)
    }
    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(Ship::class.java)

        const val IS_DEAD = 1
        const val DIED_RECENTLY = 1 shl 2
    }
}

abstract class ShipAction(val parent: SquadAction, val ship: Ship) : Serializable, State
