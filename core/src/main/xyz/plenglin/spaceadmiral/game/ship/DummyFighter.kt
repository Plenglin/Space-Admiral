package xyz.plenglin.spaceadmiral.game.ship

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.LASER
import xyz.plenglin.spaceadmiral.game.ship.weapon.HitscanFiringType
import xyz.plenglin.spaceadmiral.game.ship.weapon.WeaponMountTemplate
import xyz.plenglin.spaceadmiral.game.ship.weapon.WeaponType
import xyz.plenglin.spaceadmiral.util.Transform2D

object DummyFighter : ShipType {
    override val displayScale: Float = 1f
    override val turrets: List<WeaponMountTemplate> = listOf(
            WeaponMountTemplate(
                    transform = Transform2D(Vector2(0f, 0f)),
                    weapon = WeaponType(HitscanFiringType(10, LASER), 300, 20f),
                    firingLimit = 10f)
    )
    override val hull: Int = 100
    override val armor: Int = 100
    override val shield: Int = 100
    override val squadSize: Int = 30
    override val speed: Float = 0.5f
    override val spacing: Float = 1f
    override val defaultFormationWidth: Int = 9
}