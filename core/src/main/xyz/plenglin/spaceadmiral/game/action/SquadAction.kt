package xyz.plenglin.spaceadmiral.game.action

import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.view.model.GameStateCM
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

interface ActionDTO : Serializable {
    fun deserialize(gs: GameStateCM): ActionCM
}

interface ActionCM {
    val endPos: SquadTransform
    val eta: ETA
}
