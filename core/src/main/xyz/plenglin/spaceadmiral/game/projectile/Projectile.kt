package xyz.plenglin.spaceadmiral.game.projectile

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.GameObject
import xyz.plenglin.spaceadmiral.game.GameStateTraverser
import xyz.plenglin.spaceadmiral.game.team.Team

class Projectile(val team: Team, val pos: Vector2, val velocity: Vector2) : GameObject {
    override fun acceptTraverser(traverser: GameStateTraverser) {
        traverser.traverse(this)
    }

    fun update() {

    }

}