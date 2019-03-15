package xyz.plenglin.spaceadmiral.game.ship

interface ShipType {
    val hull: Int
    val armor: Int
    val shield: Int

    val squadSize: Int

    /**
     * How far the ship can move every tick.
     */
    val speed: Float
    val spacing: Float
    val defaultFormationWidth: Int
}