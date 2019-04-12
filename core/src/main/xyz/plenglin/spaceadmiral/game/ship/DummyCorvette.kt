package xyz.plenglin.spaceadmiral.game.ship

import xyz.plenglin.spaceadmiral.game.Health
import xyz.plenglin.spaceadmiral.game.ship.weapon.WeaponMountTemplate

object DummyCorvette : ShipType {

    override val displayName: String = "Corvette"
    override val displayScale: Float = 4f
    override val turrets: List<WeaponMountTemplate> = listOf()
    override val health: Health = Health(100, 100, 100)
    override val squadSize: Int = 1
    override val speed: Float = 80f
    override val spacing: Float = 1f
    override val defaultFormationWidth: Int = 1

    override val tadarStrength: Float = 0.01f
    override val tadarSignature: Float = 0.0001f
}