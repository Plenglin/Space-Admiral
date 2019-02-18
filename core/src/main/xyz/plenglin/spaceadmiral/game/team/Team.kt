package xyz.plenglin.spaceadmiral.game.team

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.game.squad.Squad
import java.util.*

data class Team(val gameInstance: GameInstance,
                val color: Color,
                val projectiles: MutableList<Projectile> = LinkedList(),
                val squads: MutableList<Squad> = mutableListOf()) {

    fun createSquad(template: ShipType): Squad {
        val out = Squad(template, this)
        squads.add(out)
        return out
    }
}