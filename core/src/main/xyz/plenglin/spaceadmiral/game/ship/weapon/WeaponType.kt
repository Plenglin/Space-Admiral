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
    var lastFired = 0
    var beganFiring = 0
}
