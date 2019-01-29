package xyz.plenglin.spaceadmiral.game.ship

interface ShipType {
    val hull: Int
    val armor: Int
    val shield: Int

    val squadSize: Int
    val speed: Float
    val spacing: Float
}