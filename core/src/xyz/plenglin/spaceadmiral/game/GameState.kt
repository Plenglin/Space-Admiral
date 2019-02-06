package xyz.plenglin.spaceadmiral.game

import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.team.Team

class GameState {
    val teams = mutableListOf<Team>()
}

interface GameObject {
    fun acceptTraverser(traverser: GameStateTraverser)
}

interface GameStateTraverser {
    fun traverse(squad: Squad)
    fun traverse(ship: Ship)
    fun traverse(projectile: Projectile)
}