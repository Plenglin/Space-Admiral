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

class Team constructor(val gameState: GameState,
           color: Color,
           val uuid: TeamID = nextTeamID(),
           val projectiles: MutableList<Projectile> = LinkedList(),
           squads: MutableList<Squad> = mutableListOf()) : Serializable{

    val squads get() = squadMap.values.toList()
    private val squadMap = squads.map { it.uuid.squad to it }.toMap().toMutableMap()

    val color = color.toIntBits()
    private var nextSquadId: Byte = 0

    fun createSquad(template: ShipType, sector: Sector): Squad {
        val id = SquadID(uuid, nextSquadId++)
        val out = Squad(template, this, sector, id)
        squadMap[id.squad] = out
        sector.squads[out.uuid] = out
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

    operator fun get(squadID: SquadID): Squad? {
        if (squadID.team != this.uuid) return null
        return squadMap[squadID.squad]
    }

    fun get(squadSubID: Byte): Squad? {
        return squadMap[squadSubID]
    }

}