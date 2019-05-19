package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.SquadSubID
import xyz.plenglin.spaceadmiral.TeamID

class TeamCM(val uuid: TeamID, val gameState: GameStateCM, val color: Color) {
    internal val squadMap: HashMap<SquadSubID, SquadCM> = hashMapOf()
    val squads = squadMap.values

    fun get(squadSubID: SquadSubID): SquadCM? {
        return squadMap[squadSubID]
    }

    fun isAlliedWith(other: TeamID): Boolean {
        return uuid == other
    }

    fun isAlliedWith(other: TeamCM): Boolean {
        return isAlliedWith(other.uuid)
    }

    fun controls(squad: SquadCM): Boolean {
        return squad.team == this
    }

    override fun toString(): String {
        return "TeamCM($uuid)"
    }

}
