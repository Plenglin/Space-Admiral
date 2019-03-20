package xyz.plenglin.spaceadmiral.game.ship.action

import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipAction
import xyz.plenglin.spaceadmiral.game.squad.AttackSquadAction
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler

class SwarmAttack(parent: AttackSquadAction, ship: Ship, private val targetSquad: Squad, private val orbitDistance: Float) : ShipAction(parent, ship) {

    private lateinit var target: Ship

    private fun reselectTarget() {
        target = targetSquad.ships.random()
    }

    override fun initialize(parent: StateScheduler) {
        reselectTarget()
    }

    override fun update() {
        val delta = target.transform.posGlobal.cpy().sub(ship.transform.posGlobal).nor()
        //ship.transform.posLocal.mulAdd(ship.template.speed)
        ship.transform.angleLocal = delta.angleRad()
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
