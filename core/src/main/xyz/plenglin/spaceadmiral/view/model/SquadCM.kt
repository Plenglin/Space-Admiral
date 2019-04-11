package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.action.ActionCM
import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SquadUDTO
import java.util.*
import kotlin.collections.HashMap

class SquadCM(val uuid: UUID, val team: TeamCM, val template: ShipType, val transform: SquadTransform) {

    var sector: SectorCM? = null
    val ships: MutableMap<UUID, ShipCM> = HashMap()
    var index: Int = 0
    var visible = false

    val actions = mutableListOf<ActionCM>()

    val gameState: GameStateCM get() = team.gameState
    val centerOfMass: Vector2 get() {
        val out = Vector2(0f, 0f)
        ships.forEach { _, ship ->
            out.add(ship.transform.posGlobal)
        }
        return out.scl(1f / ships.size)
    }

    fun updateWith(dto: SquadUDTO) {
        visible = true
        actions.clear()
        actions.addAll(dto.actions.map { it.deserialize(gameState) })
    }

}
