package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.squad.SquadAction
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.io.Serializable
import java.util.*

class Ship(val parent: Squad, val number: Int) : Serializable {

    val team get() = parent.team
    val gameState get() = team.gameState
    val template get() = parent.template

    var transformIndex = number
    val uuid: UUID = UUID.randomUUID()
    val transform = Transform2D(Vector2(), 0f)

    val stateScheduler = StateScheduler()

    var health = template.health.copy()
    var morale = 0f

    val turrets = template.turrets.map { it.createMount(this) }
    val velocity = Vector2()

    init {

    }

    fun onDeath() {
    }

    fun update() {
        stateScheduler.update()
        transform.posLocal.add(velocity)
        if (velocity.len2() != 0f) {
            transform.angleLocal = velocity.angleRad()
        }
        turrets.forEach {
            it.update()
        }
    }

}

abstract class ShipAction(val parent: SquadAction, val ship: Ship) : Serializable, State
