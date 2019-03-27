package xyz.plenglin.spaceadmiral.game.ship.weapon

import java.io.Serializable

data class WeaponType(
        val firingType: FiringType,
        val windupTime: Int,
        val cooldownPeriod: Int,
        val maxRange: Float
) : Serializable {

    fun createWeapon(): Weapon {
        return Weapon(this)
    }
}

class Weapon internal constructor(val parent: WeaponType) : Serializable {
    var lastFired = Long.MIN_VALUE

    fun update() {

    }

    fun onFire() {
        //nextFiring = parent.cooldownPeriod.toLong()
    }

    fun canFire(step: Long): Boolean {
        return true
    }
}
