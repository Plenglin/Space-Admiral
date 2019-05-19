package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.TeamID
import java.util.*

class TeamCM(val uuid: TeamID, val gameState: GameStateCM, val color: Color) {
    val squads = mutableListOf<SquadCM>()

    fun isAlliedWith(other: TeamID): Boolean {
        return uuid == other
    }

    fun isAlliedWith(other: TeamCM): Boolean {
        return isAlliedWith(other.uuid)
    }

    fun controls(squad: SquadCM): Boolean {
        return squad.team == this
    }
}
