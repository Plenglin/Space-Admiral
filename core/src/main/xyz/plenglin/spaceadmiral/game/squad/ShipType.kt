package xyz.plenglin.spaceadmiral.game.squad

import xyz.plenglin.spaceadmiral.game.Health
import xyz.plenglin.spaceadmiral.game.ship.weapon.WeaponMountTemplate
import java.io.Serializable

interface ShipType : Serializable {
    val displayName: String

    val health: Health

    val displayScale: Float

    val squadSize: Int

    /**
     * How far the ship moves every second.
     */
    val speed: Float
    val spacing: Float
    val defaultFormationWidth: Int

    val turrets: List<WeaponMountTemplate>
    val tadarStrength: Float
    val tadarSignature: Float

    val classification: ShipClassification

}
