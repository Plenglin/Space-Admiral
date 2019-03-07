package xyz.plenglin.spaceadmiral.game.squad

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipAction
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler
import java.io.Serializable


sealed class ETA : Serializable

object Indefinite : ETA()

class Definite(val seconds: Float) : ETA()

abstract class SquadAction(val squad: Squad, val next: SquadAction? = null) : Serializable, State {
    val timeLeft: ETA get() = Indefinite

    private var isFinished = false

    abstract fun getShipAction(ship: Ship): ShipAction?

    override fun interrupt() {
        squad.ships.forEach { it.state = null }
    }

    inner class AwaitSquadAction(ship: Ship) : ShipAction(this, ship) {
        override fun initialize(parent: StateScheduler) {
        }

        override fun update() {
        }

        override fun shouldTerminate(): Boolean {
            return isFinished
        }

        override fun interrupt() {
        }

        override fun terminate(): State? {
            return null
        }

    }
}

class MoveSquadAction(squad: Squad, target: SquadTransform) : SquadAction(squad) {
    private val transforms = target.generateChildTransforms()
    private val shipsEnRoute = squad.ships.toHashSet()

    override fun getShipAction(ship: Ship): ShipAction? = MoveShipAction(ship)

    override fun initialize(parent: StateScheduler) {

    }

    override fun update() {

    }

    override fun shouldTerminate(): Boolean = shipsEnRoute.isEmpty()

    override fun terminate(): State? {
        return null
    }

    inner class MoveShipAction(ship: Ship) : ShipAction(this@MoveSquadAction, ship) {
        private var arrived = false
        private var error = Vector2(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        private val target = transforms[ship.transformIndex]

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
            onShipFinished(ship)
        }

        override fun terminate(): State? {
            onShipFinished(ship)
            return super.terminate()
        }

    }

    private fun onShipFinished(ship: Ship) {
        shipsEnRoute.remove(ship)
    }

    companion object {
        const val EPSILON = 0.01f
    }

}

/*
class MoveSquadAction(squad: Squad, val target: Transform2D) : SquadAction(squad) {
    val actions = mutableSetOf<MoveShipAction>()

    override fun createCoroutine(): Coroutine = sequence {
        val approaching = LinkedList<Ship>()
        approaching.addAll(squad.ships)

        squad.ships.zip(squad.generateChildTransforms()).forEach { (s, t) ->
            val action = MoveShipAction(this@MoveSquadAction, s, t)
            s.currentAction = action
            actions.add(action)
        }

        while (approaching.isNotEmpty()) {
            yield(SleepTrigger(500))
        }
    }

    override val timeLeft: ETA
        get() = Definite(squad.transform.posGlobal.dst(target.posGlobal) / squad.template.speed)
}

class Attack(squad: Squad, val target: Squad) : SquadAction(squad) {
    override fun createCoroutine(): Coroutine {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isValidAction: Boolean by lazy {
        squad.team != target.team
    }

    override val timeLeft: ETA = Indefinite

}*/