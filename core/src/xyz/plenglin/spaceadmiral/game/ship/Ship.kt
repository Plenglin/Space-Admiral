package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.DamageType
import xyz.plenglin.spaceadmiral.game.Health
import xyz.plenglin.spaceadmiral.game.applyDamage
import kotlin.math.roundToInt

class Ship {
    val pos = Vector2()
    var healthInitial = Health(0, 0, 0)
    var health = Health(0, 0, 0)
    var morale = 0
}