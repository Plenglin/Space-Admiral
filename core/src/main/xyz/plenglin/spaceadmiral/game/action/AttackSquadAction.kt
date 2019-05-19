package xyz.plenglin.spaceadmiral.game.action

import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SquadID
import xyz.plenglin.spaceadmiral.game.ship.action.OrbitalSwarmAttack
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler
import xyz.plenglin.spaceadmiral.view.model.GameStateCM
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import java.util.*

class AttackSquadAction(squad: Squad, val target: Squad) : SquadAction(squad) {

    override val expectedEndPos: Vector2 get() = target.transform.transform.posGlobal

    override fun initialize(parent: StateScheduler) {
        logger.info("Initializing attack action {}", this)
        squad.ships.forEach {
            it.stateScheduler.nextState = OrbitalSwarmAttack(this, it, target, 10f)
        }
    }

    override fun update() {

    }

    override fun shouldTerminate(): Boolean {
        return target.isDead
    }

    override fun terminate(): State? {
        return null
    }

    override fun toDTO(): ActionDTO {
        return DTO(target.uuid)
    }

    private data class DTO(val target: SquadID) : ActionDTO {
        override fun deserialize(gs: GameStateCM): ActionCM {
            return CM(gs.squads[target]!!)
        }
    }

    data class CM(val target: SquadCM) : ActionCM {
        override val endPos: SquadTransform get() = target.transform
        override val eta: ETA get() = Indefinite
    }


    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(AttackSquadAction::class.java)
    }
    
}
