package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
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
        flags = flags or DIED_RECENTLY
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

    companion object {
        const val DIED_RECENTLY = 0x1
    }
}

abstract class ShipAction(val parent: SquadAction, val ship: Ship) : Serializable, State
