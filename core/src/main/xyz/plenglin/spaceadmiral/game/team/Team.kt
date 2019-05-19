package xyz.plenglin.spaceadmiral.game.team

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.SquadID
import xyz.plenglin.spaceadmiral.TeamID
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.sector.Sector
import xyz.plenglin.spaceadmiral.game.squad.ShipType
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.nextTeamID
import java.io.Serializable
import java.util.*

class Team(val gameState: GameState,
           color: Color,
           val uuid: TeamID = nextTeamID(),
           val projectiles: MutableList<Projectile> = LinkedList(),
           val squads: MutableList<Squad> = mutableListOf()) : Serializable {

    val color = color.toIntBits()
    private var nextSquadId: Byte = 0

    fun createSquad(template: ShipType, sector: Sector): Squad {
        val out = Squad(template, this, sector, SquadID(uuid, nextSquadId++))
        squads.add(out)
        gameState.squads[out.uuid] = out
        sector.squads[out.uuid] = out
        out.ships.forEach { gameState.ships[it.uuid] = it }
        return out
    }

    fun isAlliedWith(other: TeamID): Boolean {
        return this.uuid == other
    }

    fun isAlliedWith(other: Team): Boolean {
        return this.uuid == other.uuid
    }

    override fun toString(): String {
        return "Team($uuid)"
    }

}