package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.Health
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.io.Serializable
import java.util.*

class Ship(val parent: Squad) : Serializable {

    val uuid: UUID = UUID.randomUUID()
    val transform = Transform2D(Vector2(), 0f)

    @Transient
    val stateScheduler = StateScheduler()

    var health = Health(0, 0, 0)
    var morale = 0f

    fun onDeath() {
        parent.ships.remove(this)
    }

    fun update() {
        stateScheduler.update()
    }

}

sealed class ShipAction(val ship: Ship) : Serializable, State

class MoveShipAction(val target: Transform2D, ship: Ship) : ShipAction(ship) {
    private var arrived = false
    private var error = Vector2(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)

    override fun initialize(parent: StateScheduler) {
        ship.transform.angleLocal = target.posGlobal.angle(ship.transform.posGlobal)
    }

    override fun update() {
        error = target.posGlobal.cpy().sub(ship.transform.posGlobal)
        val delta = error.setLength(ship.parent.template.speed)
        ship.transform.angleLocal = delta.angle()
        ship.transform.posGlobal.add(delta)
    }

    override fun shouldTerminate(): Boolean {
        return error.len2() < EPSILON
    }

    override fun interrupt() {
    }

    override fun terminate(): State? {
        return null
    }

    companion object {
        const val EPSILON = 0.01f
    }

}

/*
sealed class ShipAction(val ship: Ship) : Serializable {
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
        return error.len2() < EPSILON
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
        const val EPSILON = 0.01f
    }

}*/
