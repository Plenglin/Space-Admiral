package xyz.plenglin.spaceadmiral.game.action

import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.Transform2D
import xyz.plenglin.spaceadmiral.game.ship.MoveShipAction
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.util.Coroutine
import xyz.plenglin.spaceadmiral.util.Future
import xyz.plenglin.spaceadmiral.util.SleepTrigger
import java.util.*


sealed class ETA {
}

object Indefinite : ETA() {

}

class Definite(val seconds: Float) : ETA() {

}

sealed class SquadAction(val squad: Squad) {
    var future: Future? = null
    open val isValidAction: Boolean = true
    open val timeLeft: ETA = Indefinite
    abstract fun createCoroutine(): Coroutine
}

class MoveSquadAction(squad: Squad, val target: Transform2D) : SquadAction(squad) {
    val actions = mutableSetOf<MoveShipAction>()

    override fun createCoroutine(): Coroutine = sequence {
        val approaching = LinkedList<Ship>()
        approaching.addAll(squad.ships)

        squad.ships.zip(squad.generateRelativeTransforms()).forEach { (s, t) ->
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

}