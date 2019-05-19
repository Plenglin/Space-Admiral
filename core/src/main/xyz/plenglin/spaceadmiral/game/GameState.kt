package xyz.plenglin.spaceadmiral.game

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.*
import xyz.plenglin.spaceadmiral.game.sector.Sector
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.squad.WarpBubble
import xyz.plenglin.spaceadmiral.game.team.Team
import java.io.Serializable

class GameState : Serializable {
    var time: Long = 0

    private val sectorMap = HashMap<SectorID, Sector>()
    val sectors get() = sectorMap.values.toList()

    private val teamMap = HashMap<TeamID, Team>()
    val teams get() = teamMap.values.toList()

    val warpBubbles = HashMap<WarpBubbleID, WarpBubble>()

    operator fun get(teamID: TeamID): Team? {
        return teamMap[teamID]
    }

    operator fun get(squadID: SquadID): Squad? {
        return this[squadID.team]?.get(squadID)
    }

    operator fun get(pos: SectorID): Sector {
        var out = sectorMap[pos]
        if (out == null) {
            out = Sector(this, pos)
            sectorMap[pos] = out
        }
        return out
    }

    fun createTeam(color: Color, uuid: TeamID = nextTeamID()): Team {
        val out = Team(this, color, uuid = uuid)
        teamMap[out.uuid] = out
        return out
    }
}
