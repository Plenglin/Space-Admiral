package xyz.plenglin.spaceadmiral.game.squad

import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ActionDTO
import xyz.plenglin.spaceadmiral.util.State
import java.io.Serializable


sealed class ETA : Serializable

object Indefinite : ETA()

class Definite(val seconds: Float) : ETA()


abstract class SquadAction(val squad: Squad) : Serializable, State {
    private var isFinished = false
    open val expectedEndPos get() = squad.transform.transform.posGlobal

    override fun interrupt() {
        squad.ships.forEach { it.stateScheduler.interrupt() }
    }

    abstract fun toDTO(): ActionDTO
}
