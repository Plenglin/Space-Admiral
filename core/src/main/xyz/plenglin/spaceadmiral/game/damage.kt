package xyz.plenglin.spaceadmiral.game

import java.io.Serializable
import kotlin.math.roundToInt


/**
 * @param name The name.
 * @param kShield Shield damage multiplier.
 * @param kArmor Armor damage multiplier.
 * @param kHull Hull damage multiplier.
 */
data class DamageType(val name: String,
                      val kHull: Float = 1.0f,
                      val kArmor: Float = 1.0f,
                      val kShield: Float = 1.0f) : Serializable {
    init {
        assert(kHull >= 0) { "One does not simply ignore hull" }
    }
}

data class Health(var hull: Int, var armor: Int, var shield: Int) : Serializable {
    fun applyDamage(dmg: Int, dmgType: DamageType) {
        var dmgLeft: Int
        applyDamage(shield, dmg, dmgType.kShield).let { (s, dl) ->
            shield = s
            dmgLeft = dl
        }
        if (dmgLeft == 0) {
            return
        }

        applyDamage(armor, dmg, dmgType.kArmor).let { (s, dl) ->
            armor = s
            dmgLeft = dl
        }
        if (dmgLeft == 0) {
            return
        }

        applyDamage(hull, dmg, dmgType.kHull).let { (s, dl) ->
            hull = s
            dmgLeft = dl
        }
        if (dmgLeft == 0) {
            return
        }
    }

    val totalHealth get() = hull + armor + shield
    val isDead get() = hull <= 0

    fun getEffectiveHealth(dmgType: DamageType): Int {
        var ehp = 0
        if (dmgType.kShield == 0f) {
            return Int.MAX_VALUE
        } else if (dmgType.kShield > 0) {
            ehp += (shield / dmgType.kShield).roundToInt()
        }
        if (dmgType.kArmor == 0f) {
            return Int.MAX_VALUE
        } else if (dmgType.kArmor > 0) {
            ehp += (armor / dmgType.kArmor).roundToInt()
        }
        if (dmgType.kHull == 0f) {
            return Int.MAX_VALUE
        } else if (dmgType.kShield > 0) {
            ehp += (hull / dmgType.kShield).roundToInt()
        }
        return ehp
    }

}

val LASER = DamageType("laser", 0.5f, 1.0f, 1.0f)
val PROJECTILE = DamageType("laser", 1.0f, 3.0f, 1.0f)
val EXPLOSIVE = DamageType("laser", 0.5f, 3.0f, 1.0f)

/**
 * Apply damage to health, given a multiplier
 * @return tuple of (health left, damage left)
 */
fun applyDamage(health: Int, dmg: Int, multiplier: Float): Pair<Int, Int> {
    return if (multiplier > 0) {  // Is the multiplier positive (i.e. we can affect this health type)?
        val dmgNeeded = (health / multiplier).roundToInt()
        if (dmg > dmgNeeded) {  // Do we have enough damage to pop the shield?
            Pair(health, dmg - dmgNeeded)
        } else {
            val scaledDmg = dmg * multiplier
            Pair((health - scaledDmg).roundToInt(), 0)
        }
    } else if (multiplier == 0f) {  // Is the multiplier zero (i.e. we cannot affect this health type)?
        Pair(health, 0)
    } else {  // Is the multiplier negative (i.e. we ignore this health type)?
        Pair(health, dmg)
    }
}
