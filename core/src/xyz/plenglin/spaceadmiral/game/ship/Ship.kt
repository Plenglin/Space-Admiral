package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.*
import xyz.plenglin.spaceadmiral.game.action.MoveSquadAction
import xyz.plenglin.spaceadmiral.util.Transform2D

class Ship(val parent: Squad) {
    val transform = Transform2D(Vector2(), 0f)
    var currentAction: ShipAction? = null

    var healthInitial = Health(0, 0, 0)
    var health = Health(0, 0, 0)
    var morale = 0

    fun onDeath() {
        parent.ships.remove(this)
    }

    fun update() {
        currentAction?.coroutine?.next()
    }
}

sealed class ShipAction(val ship: Ship) {
    lateinit var coroutine: Iterator<Long>
    abstract fun createCoroutine(): Sequence<Long>
}

class MoveShipAction(val parent: MoveSquadAction, ship: Ship, val target: Transform2D) : ShipAction(ship) {
    override fun createCoroutine(): Sequence<Long> = sequence {

    }

}