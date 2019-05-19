package xyz.plenglin.spaceadmiral.game.ship.weapon

import xyz.plenglin.spaceadmiral.TurretID
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


class WeaponMount internal constructor(template: WeaponMountTemplate, val ship: Ship, val uuid: TurretID) : Serializable {

    val gameState: GameState get() = ship.gameState
    val transform: Transform2D = template.transform.clone().apply {
        this.parent = ship.transform
    }
    val weapon: Weapon? = template.weapon?.createWeapon()
    var enabled: Boolean = true
    var target: Ship? = null

    private fun fire(target: Ship) {
        weapon?.let {
            it.parent.firingType.fireFrom(ship.sector!!, this, target)
            it.onFire(gameState.time)
        }
    }

    private fun canFire(target: Ship): Boolean {
        if (!enabled) return false
        if (weapon == null) return false
        if (!weapon.canFire(gameState.time)) return false

        val dst2 = weapon.parent.maxRange.let { it * it }
        return target.transform.posGlobal.dst2(this.transform.posGlobal) <= dst2
    }

    fun update() {
        val target = target
        if (target != null && canFire(target)) {
            fire(target)
        }
    }
}
