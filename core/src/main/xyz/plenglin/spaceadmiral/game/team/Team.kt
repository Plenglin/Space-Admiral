package xyz.plenglin.spaceadmiral.game.team

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.game.squad.Squad
import java.io.Serializable
import java.util.*

data class Team(val parent: GameState,
                val color: Color,
                val uuid: UUID = UUID.randomUUID(),
                val projectiles: MutableList<Projectile> = LinkedList(),
                val squads: MutableList<Squad> = mutableListOf()) : Serializable {

    private var nextSquadIndex = 0

    fun createSquad(template: ShipType): Squad {
        val out = Squad(template, this, nextSquadIndex)
        nextSquadIndex++
        squads.add(out)
        parent.squads[out.uuid] = out
        out.ships.forEach { parent.ships[it.uuid] = it }
        return out
    }

    fun isAlliedWith(other: UUID): Boolean {
        return this.uuid == other
    }

    fun isAlliedWith(other: Team): Boolean {
        return this.uuid == other.uuid
    }
}