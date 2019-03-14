package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.Health
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.squad.SquadAction
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.io.Serializable
import java.util.*

class Ship(val parent: Squad, val number: Int) : Serializable {

    var transformIndex = number
    val uuid: UUID = UUID.randomUUID()
    val transform = Transform2D(Vector2(), 0f)

    @Transient
    private val stateScheduler = StateScheduler()
    var state: ShipAction?
        get() = stateScheduler.currentState as ShipAction?
        set(value) {
            stateScheduler.nextState = value
        }

    var health = Health(0, 0, 0)
    var morale = 0f
    val team get() = parent.team

    fun onDeath() {
        parent.ships.remove(this)
    }

    fun update() {
        stateScheduler.update()
    }

}

abstract class ShipAction(val parent: SquadAction, val ship: Ship) : Serializable, State
