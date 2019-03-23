package xyz.plenglin.spaceadmiral.game.ship

import xyz.plenglin.spaceadmiral.game.ship.weapon.WeaponMountType

class DummyFighter : ShipType {
    override val turrets: List<WeaponMountType> = listOf()
    override val hull: Int = 100
    override val armor: Int = 100
    override val shield: Int = 100
    override val squadSize: Int = 30
    override val speed: Float = 0.5f
    override val spacing: Float = 1f
    override val defaultFormationWidth: Int = 9
}