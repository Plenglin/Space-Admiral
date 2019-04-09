package xyz.plenglin.spaceadmiral.game.ship.weapon

import xyz.plenglin.spaceadmiral.game.DamageType
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.ship.Ship
import java.io.Serializable
import java.util.*

sealed class FiringType : Serializable {
    abstract fun fireFrom(gs: GameState, mount: WeaponMount, target: Ship)
}

sealed class FiringEvent : Serializable {
    abstract val source: FiringType
    abstract val mount: UUID
    abstract val target: UUID
}

class HitscanFiringType(val damage: Int, val damageType: DamageType) : FiringType() {
    override fun fireFrom(gs: GameState, mount: WeaponMount, target: Ship) {
        val hitChance = 0.5f  // TODO FIGURE OUT A FORMULA
        if (Math.random() < hitChance) {
            gs.firingEvents.add(HitscanFiringEvent(true, this, mount.uuid, target.uuid))
            target.health.applyDamage(damage, damageType)
        } else {
            gs.firingEvents.add(HitscanFiringEvent(false, this, mount.uuid, target.uuid))
        }
    }
}

data class HitscanFiringEvent(
        val success: Boolean,
        override val source: HitscanFiringType,
        override val mount: UUID,
        override val target: UUID) : FiringEvent()

/*
class ProjectileFiringType(val projectile: ProjectileFactory) : FiringType() {
    override fun fireFrom(gs: GameState, mount: WeaponMount, target: Ship) {
        // TODO MAKE PROJECTILES A THING
        val obj = projectile.createProjectile(mount.transform, target)
        gs.projectiles[obj.id] = obj
    }
}
*/

