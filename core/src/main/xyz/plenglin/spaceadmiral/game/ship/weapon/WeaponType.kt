package xyz.plenglin.spaceadmiral.game.ship.weapon

import java.io.Serializable

data class WeaponType(
        val firingType: FiringType,
        val cooldownPeriod: Int,
        val maxRange: Float
) : Serializable {

    fun createWeapon(): Weapon {
        return Weapon(this)
    }
}

class Weapon internal constructor(val parent: WeaponType) : Serializable {
    var nextFiring = Long.MIN_VALUE

    fun onFire(time: Long) {
        nextFiring = time + parent.cooldownPeriod
    }

    fun canFire(time: Long): Boolean {
        return time >= nextFiring
    }
}
