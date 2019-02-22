package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.Health
import xyz.plenglin.spaceadmiral.game.action.MoveSquadAction
import xyz.plenglin.spaceadmiral.game.data.ShipDTO
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.util.*

class Ship(val parent: Squad) {

    val uuid: UUID = UUID.randomUUID()
    val transform = Transform2D(Vector2(), 0f)

    var currentAction: ShipAction? = null
    var currentTarget: Ship? = null

    var healthInitial = Health(0, 0, 0)
    var health = Health(0, 0, 0)
    var morale = 0

    var hasDelta = false

    fun onDeath() {
        parent.ships.remove(this)
        hasDelta = true
    }

    fun updateInitial() {
        hasDelta = false
        currentAction?.let {
            hasDelta = it.update()
            if (it.shouldTerminate()) {
                it.terminate()
                currentAction = null
            }
        }
    }

    fun createDTO(): ShipDTO {
        return ShipDTO(uuid, transform)
    }
}

sealed class ShipAction(val ship: Ship) {
    lateinit var coroutine: Iterator<Long>
    abstract fun initialize()
    abstract fun update(): Boolean
    abstract fun shouldTerminate(): Boolean
    abstract fun terminate()
}

class MoveShipAction(val parent: MoveSquadAction, ship: Ship, val target: Transform2D) : ShipAction(ship) {

    private var error = Vector2(10000f, 1000f)
    private var arrived = false

    override fun shouldTerminate(): Boolean {
        return error.len2() < SHIP_EPSILON
    }

    override fun initialize() {
        ship.transform.angleLocal = target.posGlobal.angle(ship.transform.posGlobal)
    }

    override fun update(): Boolean {
        error = target.posGlobal.cpy().sub(ship.transform.posGlobal)
        val delta = error.setLength(ship.parent.template.speed)
        ship.transform.angleLocal = delta.angle()
        ship.transform.posGlobal.add(delta)
        return true
    }

    override fun terminate() {
        parent.actions.remove(this)
    }

    companion object {
        const val SHIP_EPSILON = 0.01f
    }

}