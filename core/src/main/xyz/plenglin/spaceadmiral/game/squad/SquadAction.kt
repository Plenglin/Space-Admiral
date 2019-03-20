package xyz.plenglin.spaceadmiral.game.squad

import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipAction
import xyz.plenglin.spaceadmiral.util.State
import java.io.Serializable


sealed class ETA : Serializable

object Indefinite : ETA()

class Definite(val seconds: Float) : ETA()


abstract class SquadAction(val squad: Squad) : Serializable, State {
    open val timeLeft: ETA get() = Indefinite

    private var isFinished = false
    open val expectedEndPos get() = squad.transform.transform.posGlobal

    abstract fun getShipAction(ship: Ship): ShipAction?


    override fun interrupt() {
        squad.ships.forEach { it.stateScheduler.interrupt() }
    }
}
