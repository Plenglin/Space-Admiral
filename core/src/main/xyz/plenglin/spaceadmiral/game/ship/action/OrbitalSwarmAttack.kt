package xyz.plenglin.spaceadmiral.game.ship.action

import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.ShipAction
import xyz.plenglin.spaceadmiral.game.squad.AttackSquadAction
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler
import kotlin.random.Random

class OrbitalSwarmAttack(parent: AttackSquadAction, ship: Ship, private val targetSquad: Squad, private val orbitDistance: Float) : ShipAction(parent, ship) {

    private val perp = if (Random.nextBoolean()) 1 else -1
    private var target: Ship? = null
    private var orbit2 = 0f

    private fun reselectTarget() {
        if (targetSquad.ships.isEmpty()) {
            target = null
        } else {
            target = targetSquad.ships.random()
            orbit2 = orbitDistance + (Random.nextFloat() + Random.nextFloat() + Random.nextFloat()) - 1.5f
            orbit2 *= orbit2
        }
        ship.turrets.forEach {
            it.target = target
        }
    }

    override fun initialize(parent: StateScheduler) {
        reselectTarget()
    }

    override fun update() {
        val target = target ?: return
        if (target.health.isDead) {
            reselectTarget()
        }

        val radius = ship.transform.posGlobal.cpy().sub(target.transform.posGlobal)
        val r2 = radius.len2()

        val tangent = radius.cpy().rotate90(perp)

        val error = -(Math.sqrt(r2.toDouble()) - Math.sqrt(orbit2.toDouble())).toFloat()
        ship.velocity.set(radius).scl(error * 0.25f).add(tangent).setLength(ship.template.speed)
    }

    override fun shouldTerminate(): Boolean {
        return target == null
    }

    override fun interrupt() {
        ship.turrets.forEach {
            it.target = null
        }
        ship.velocity.set(0f, 0f)
    }

    override fun terminate(): State? {
        ship.turrets.forEach {
            it.target = null
        }
        ship.velocity.set(0f, 0f)
        return null
    }

}
