package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.graphics.Color
import java.util.*

class TeamCM(val uuid: UUID, val gameState: GameStateCM, val color: Color) {
    val squads = mutableListOf<SquadCM>()

    fun isAlliedWith(other: UUID): Boolean {
        return uuid == other
    }

    fun isAlliedWith(other: TeamCM): Boolean {
        return isAlliedWith(other.uuid)
    }
}
