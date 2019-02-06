package xyz.plenglin.spaceadmiral.game.team

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.GameObject
import xyz.plenglin.spaceadmiral.game.GameStateTraverser
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.game.squad.Squad
import java.util.*

class Team(val gameInstance: GameInstance, val color: Color) : GameObject {
    override fun acceptTraverser(traverser: GameStateTraverser) {
        squads.forEach(traverser::traverse)
    }

    val projectiles = LinkedList<Projectile>()
    val squads = mutableListOf<Squad>()

    fun createSquad(squad: ShipType): Squad {
        return Squad(squad, this).apply {
            generateRelativeTransforms()
        }
    }
}