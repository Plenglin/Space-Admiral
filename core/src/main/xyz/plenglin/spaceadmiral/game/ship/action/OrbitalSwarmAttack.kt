package xyz.plenglin.spaceadmiral.game.ship.action

import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipAction
import xyz.plenglin.spaceadmiral.game.squad.AttackSquadAction
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler
import kotlin.random.Random

class OrbitalSwarmAttack(parent: AttackSquadAction, ship: Ship, private val targetSquad: Squad, orbitDistance: Float) : ShipAction(parent, ship) {

    private lateinit var target: Ship
    private var perp = 1
    private val orbit2 = orbitDistance * orbitDistance

    private fun reselectTarget() {
        target = targetSquad.ships.random()
        perp = if (Random.nextBoolean()) 1 else -1
    }

    override fun initialize(parent: StateScheduler) {
        reselectTarget()
    }

    override fun update() {
        val radius = ship.transform.posGlobal.cpy().sub(target.transform.posGlobal)
        val r2 = radius.len2()

        val tangent = radius.cpy().rotate90(perp)

        if (r2 < orbit2) {
            ship.velocity.set(radius).add(tangent).setLength(ship.template.speed)
        } else {
            val error = (Math.sqrt(orbit2.toDouble()) - Math.sqrt(r2.toDouble())).toFloat()
            ship.velocity.set(radius).scl(error).add(tangent).setLength(ship.template.speed)
        }


        //ship.velocity.set(radius).add(radius, r2 - ).setLength(ship.template.speed))
    }

    override fun shouldTerminate(): Boolean {
        return targetSquad.ships.isEmpty()
    }

    override fun interrupt() {

    }

    override fun terminate(): State? {
        return null
    }

}
