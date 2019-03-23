package xyz.plenglin.spaceadmiral.game.squad

import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.ship.action.OrbitalSwarmAttack
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler

class AttackSquadAction(squad: Squad, val target: Squad) : SquadAction(squad) {

    override val expectedEndPos: Vector2 get() = target.transform.transform.posGlobal

    override fun initialize(parent: StateScheduler) {
        logger.info("Initializing attack action {}", this)
        squad.ships.forEach {
            it.stateScheduler.nextState = OrbitalSwarmAttack(this, it, target, 5f)
        }
    }

    override fun update() {

    }

    override fun shouldTerminate(): Boolean {
        return false
    }

    override fun terminate(): State? {
        return null
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(AttackSquadAction::class.java)
    }
    
}