package xyz.plenglin.spaceadmiral.game.ship.action

import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipAction
import xyz.plenglin.spaceadmiral.game.squad.AttackSquadAction
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler
import kotlin.random.Random

class OrbitalSwarmAttack(parent: AttackSquadAction, ship: Ship, private val targetSquad: Squad, private val orbitDistance: Float) : ShipAction(parent, ship) {

    private lateinit var target: Ship
    private var perp = 1
    private var orbit2 = 0f

    private fun reselectTarget() {
        target = targetSquad.ships.random()
        perp = if (Random.nextBoolean()) 1 else -1
        orbit2 = orbitDistance + (Random.nextFloat() + Random.nextFloat() + Random.nextFloat()) - 1.5f
        orbit2 *= orbit2
    }

    override fun initialize(parent: StateScheduler) {
        reselectTarget()
    }

    override fun update() {
        val radius = ship.transform.posGlobal.cpy().sub(target.transform.posGlobal)
        val r2 = radius.len2()

        val tangent = radius.cpy().rotate90(perp)

        val error = -(Math.sqrt(r2.toDouble()) - Math.sqrt(orbit2.toDouble())).toFloat()
        ship.velocity.set(radius).scl(error * 0.25f).add(tangent).setLength(ship.template.speed)

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
