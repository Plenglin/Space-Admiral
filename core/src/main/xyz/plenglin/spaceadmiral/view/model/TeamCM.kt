package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.graphics.Color
import java.util.*

class TeamCM(val uuid: UUID, val gameState: GameStateCM, val squads: List<SquadCM>) {
    val color: Color = TODO() // Color.argb8888ToColor(color, it)

    fun isAlliedWith(other: UUID): Boolean {
        return uuid == other
    }

    fun isAlliedWith(other: TeamCM): Boolean {
        return isAlliedWith(other.uuid)
    }
}
