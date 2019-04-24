package xyz.plenglin.spaceadmiral.game

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.util.IntVector2
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class GameState : Serializable {
    var time: Long = 0

    val sectors = HashMap<IntVector2, Sector>()
    val teams = HashMap<UUID, Team>()
    val squads = HashMap<UUID, Squad>()
    val ships = HashMap<UUID, Ship>()
    val projectiles = HashMap<UUID, Projectile>()

    fun getSector(pos: IntVector2): Sector {
        var out = sectors[pos]
        if (out == null) {
            out = Sector(this, pos)
            sectors[pos] = out
        }
        return out
    }

    fun createTeam(color: Color, uuid: UUID = UUID.randomUUID()): Team {
        val out = Team(this, color, uuid = uuid)
        teams[out.uuid] = out
        return out
    }
}
