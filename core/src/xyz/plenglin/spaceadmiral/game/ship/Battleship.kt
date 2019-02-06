package xyz.plenglin.spaceadmiral.game.ship

class Battleship : ShipType {
    override val hull: Int = 100
    override val armor: Int = 100
    override val shield: Int = 100
    override val squadSize: Int = 3
    override val speed: Float = 10f
    override val spacing: Float = 3f
    override val defaultFormationWidth: Int = 3

}