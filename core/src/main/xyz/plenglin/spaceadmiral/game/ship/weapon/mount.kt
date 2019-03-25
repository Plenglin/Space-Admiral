package xyz.plenglin.spaceadmiral.game.ship.weapon

import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.io.Serializable

data class WeaponMountType(
        /**
         * Where the mount is on the ship
         */
        val transform: Transform2D,
        /**
         * What kind of weapon is mounted on it
         */
        var weapon: WeaponType? = null,
        /**
         * How far from its center it can turn, in radians
         */
        var turningLimit: Float = 4f,
        /**
         * How far from the target it can begin firing at
         */
        var firingLimit: Float = 0f
) : Serializable

class WeaponMount(val template: WeaponMountType, val ship: Ship) : Serializable {
    val transform: Transform2D = template.transform.clone().apply {
        this.parent = ship.transform
    }
    var enabled: Boolean = true
    var lastFired = 0
    fun fire() {

    }
}

enum class MountType : Serializable {
    ALL_DIRECTIONAL, FRONT, LEFT_BROADSIDE, RIGHT_BROADSIDE, REAR
}
