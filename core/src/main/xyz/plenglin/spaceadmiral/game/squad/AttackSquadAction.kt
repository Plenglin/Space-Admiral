package xyz.plenglin.spaceadmiral.game.squad

import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipAction
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler

class AttackSquadAction(squad: Squad, val target: Squad) : SquadAction(squad) {
    override fun getShipAction(ship: Ship): ShipAction? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initialize(parent: StateScheduler) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun shouldTerminate(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun terminate(): State? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
