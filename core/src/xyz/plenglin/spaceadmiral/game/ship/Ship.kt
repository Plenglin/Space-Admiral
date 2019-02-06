package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.*
import xyz.plenglin.spaceadmiral.game.action.MoveSquadAction
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.Transform2D

class Ship(val parent: Squad) : GameObject {

    override fun acceptTraverser(traverser: GameStateTraverser) {
        traverser.traverse(this)
    }

    val transform = Transform2D(Vector2(), 0f)
    var currentAction: ShipAction? = null

    var healthInitial = Health(0, 0, 0)
    var health = Health(0, 0, 0)
    var morale = 0

    fun onDeath() {
        parent.ships.remove(this)
    }

    fun update() {
        currentAction?.update()
    }
}

sealed class ShipAction(val ship: Ship) {
    lateinit var coroutine: Iterator<Long>
    abstract fun initialize()
    abstract fun update(): Boolean
    abstract fun terminate()
}

class MoveShipAction(val parent: MoveSquadAction, ship: Ship, val target: Transform2D) : ShipAction(ship) {
    override fun initialize() {
        ship.transform.angleLocal = target.posGlobal.angle(ship.transform.posGlobal)
    }

    override fun update(): Boolean {
        val error = target.posGlobal.cpy().sub(ship.transform.posGlobal)
        val delta = error.setLength(ship.parent.template.speed)
        ship.transform.angleLocal = delta.angle()
        ship.transform.posGlobal.add(delta)
        return error.len2() < 10
    }

    override fun terminate() {
        parent.actions.remove(this)
    }

}