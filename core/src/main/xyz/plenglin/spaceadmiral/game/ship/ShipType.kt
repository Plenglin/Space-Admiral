package xyz.plenglin.spaceadmiral.game.ship

import xyz.plenglin.spaceadmiral.game.Health
import xyz.plenglin.spaceadmiral.game.ship.weapon.WeaponMountTemplate
import java.io.Serializable

interface ShipType : Serializable {
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
}