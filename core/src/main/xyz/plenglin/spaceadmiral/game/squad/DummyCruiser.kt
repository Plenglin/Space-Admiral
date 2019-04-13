package xyz.plenglin.spaceadmiral.game.squad

import xyz.plenglin.spaceadmiral.game.Health
import xyz.plenglin.spaceadmiral.game.ship.weapon.WeaponMountTemplate

object DummyCruiser : ShipType {
    override val displayName: String = "Cruiser"

    override val health: Health = Health(1000, 400, 100)
    override val displayScale: Float = 15f
    override val squadSize: Int = 1
    override val speed: Float = 25f
    override val spacing: Float = 1f
    override val defaultFormationWidth: Int = 1
    override val turrets: List<WeaponMountTemplate> = listOf()
    override val tadarStrength: Float = 1f
    override val tadarSignature: Float = 1f
    override val classification: ShipClassification = ShipClassification.CRUISER
    override val mass: Float = 10f

    override val repulsion: RepulsionField? = RepulsionField(30f, 50f)

}