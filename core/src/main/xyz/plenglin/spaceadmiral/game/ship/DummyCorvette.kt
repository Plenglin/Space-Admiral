package xyz.plenglin.spaceadmiral.game.ship

import xyz.plenglin.spaceadmiral.game.ship.weapon.WeaponMountTemplate

object DummyCorvette : ShipType {
    override val displayScale: Float = 4f
    override val turrets: List<WeaponMountTemplate> = listOf()
    override val hull: Int = 100
    override val armor: Int = 100
    override val shield: Int = 100
    override val squadSize: Int = 1
    override val speed: Float = 0.2f
    override val spacing: Float = 1f
    override val defaultFormationWidth: Int = 1
}