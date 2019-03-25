package xyz.plenglin.spaceadmiral.game.ship.action

import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipAction
import xyz.plenglin.spaceadmiral.game.squad.AttackSquadAction
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler

class DivingSwarmAttack(parent: AttackSquadAction, ship: Ship, private val targetSquad: Squad, private val turningRate: Float) : ShipAction(parent, ship) {
    override fun initialize(parent: StateScheduler) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shouldTerminate(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun interrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun terminate(): State? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}