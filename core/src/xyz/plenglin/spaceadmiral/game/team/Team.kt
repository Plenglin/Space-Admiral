package xyz.plenglin.spaceadmiral.game.team

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.GameObject
import xyz.plenglin.spaceadmiral.game.GameStateTraverser
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.game.squad.Squad
import java.util.*

data class Team(val gameInstance: GameInstance,
                val color: Color,
                val projectiles: MutableList<Projectile> = LinkedList(),
                val squads: MutableList<Squad> = mutableListOf())
    : GameObject {

    override fun acceptTraverser(traverser: GameStateTraverser) {
        squads.forEach(traverser::traverse)
    }

    fun createSquad(squad: ShipType): Squad {
        val squad = Squad(squad, this).apply {
            generateRelativeTransforms()
        }
        squads.add(squad)
        return squad
    }
}