package xyz.plenglin.spaceadmiral.game

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.team.Team
import java.util.*
import kotlin.collections.HashMap

class GameState {

    val team = HashMap<UUID, Team>()
    val squads = HashMap<UUID, Squad>()
    val ships = HashMap<UUID, Ship>()
    val projectiles = HashMap<UUID, Projectile>()

    fun createTeam(color: Color): Team {
        val out = Team(this, color)
        team[out.uuid] = out
        return out
    }

}
