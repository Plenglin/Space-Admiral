package xyz.plenglin.spaceadmiral.game

import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import java.util.*

class GameState {
    val ships = HashMap<UUID, Ship>()
    val projectiles = HashMap<UUID, Projectile>()
}
