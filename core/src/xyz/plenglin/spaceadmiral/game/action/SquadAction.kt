package xyz.plenglin.spaceadmiral.game.action

import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.Transform2D
import xyz.plenglin.spaceadmiral.game.ship.MoveShipAction
import xyz.plenglin.spaceadmiral.game.ship.Ship
import java.util.*


sealed class ETA {
}

object Indefinite : ETA() {

}

class Definite(val seconds: Float) : ETA() {

}

sealed class SquadAction(val squad: Squad) {
    open val isValidAction: Boolean = true
    open val timeLeft: ETA = Indefinite
    abstract fun createCoroutine(): Sequence<Long>
}

class MoveSquadAction(squad: Squad, val target: Transform2D) : SquadAction(squad) {
    lateinit var actions: MutableSet<MoveShipAction>

    override fun createCoroutine(): Sequence<Long> = sequence {
        val approaching = LinkedList<Ship>()
        approaching.addAll(squad.ships)
        actions = squad.ships.zip(squad.generateRelativeTransforms()).map { (s, t) ->
            val action = MoveShipAction(this@MoveSquadAction, s, t)
            s.currentAction = action
            action
        }.toMutableSet()

        while (approaching.isNotEmpty()) {
            yield(500L)
        }
    }

    override val timeLeft: ETA
        get() = Definite(squad.transform.posGlobal.dst(target.posGlobal) / squad.template.speed)
}

class Attack(squad: Squad, val target: Squad) : SquadAction(squad) {
    override fun createCoroutine(): Sequence<Long> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isValidAction: Boolean by lazy {
        squad.team != target.team
    }

    override val timeLeft: ETA = Indefinite

}