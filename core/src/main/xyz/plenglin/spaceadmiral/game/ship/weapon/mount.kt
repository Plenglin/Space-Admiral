package xyz.plenglin.spaceadmiral.game.ship.weapon

import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.io.Serializable

data class WeaponMountTemplate(
        /**
         * Where the mount is on the ship
         */
        val transform: Transform2D,
        /**
         * What kind of weapon is mounted on it
         */
        val weapon: WeaponType? = null,
        /**
         * How far from its center it can turn, in radians
         */
        val turningLimit: Float = 4f,
        /**
         * How far from the target it can begin firing at
         */
        val firingLimit: Float = 0f
) : Serializable

class WeaponMount(val template: WeaponMountTemplate, val ship: Ship) : Serializable {
    val transform: Transform2D = template.transform.clone().apply {
        this.parent = ship.transform
    }
    val weapon = template.weapon?.createWeapon()
    var enabled: Boolean = true

    private fun fire(gs: GameState, target: Ship) {
        if (weapon == null) {
            return
        }
        /*if (weapon.canFire(gs.time)) {
            weapon.parent.firingType.fireFrom(gs, this, target)
            weapon.onFire()
        }*/
    }

    fun update(gs: GameState, target: Ship?) {
        if (target != null) {
            fire(gs, target)
        }
    }
}
