package xyz.plenglin.spaceadmiral.game.team

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.game.GameObject
import xyz.plenglin.spaceadmiral.game.GameStateTraverser
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.squad.Squad
import java.util.*

class Team(val color: Color) : GameObject {
    override fun acceptTraverser(traverser: GameStateTraverser) {
        squads.forEach(traverser::traverse)
    }

    val projectiles = LinkedList<Projectile>()
    val squads = mutableListOf<Squad>()
}