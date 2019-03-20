package xyz.plenglin.spaceadmiral.game.ship.weapon

import java.io.Serializable

data class WeaponType(
        val firingType: FiringType,
        val chargePeriod: Float,
        val cooldownPeriod: Float,
        val maxRange: Float
) : Serializable {

    fun createWeapon(): Weapon {
        return Weapon(this)
    }
}

class Weapon internal constructor(val parent: WeaponType) : Serializable {

}
