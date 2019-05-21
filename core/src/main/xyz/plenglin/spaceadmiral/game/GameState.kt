package xyz.plenglin.spaceadmiral.game

import com.badlogic.gdx.graphics.Color
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.*
import xyz.plenglin.spaceadmiral.game.sector.Sector
import xyz.plenglin.spaceadmiral.game.ship.Ship
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

    fun update() {
        logger.trace("update {}", time)

        // Sector update
        for (sector in sectors) {
            sector.updateInitial()
        }

        val repulsors = mutableListOf<Ship>()

        for (team in teams) {
            for (squad in team.squads) {
                // Squad update
                squad.update()

                // Ship initial logic update
                for (ship in squad.ships) {
                    ship.updateLogic()
                }

                // Process repulsors
                if (squad.template.repulsion == null || squad.sector == null) continue  // Do not process
                for (ship in squad.ships) {
                    repulsors.add(ship)
                }
            }
        }

        for (bubble in warpBubbles.values) {
            if (bubble.hasArrived(time)) {
                val sector = this[bubble.endSector]

                logger.info("{} has arrived at {}", bubble, sector)
                warpBubbles.remove(bubble.uuid)

                for (squad in bubble.squads) {
                    sector.insertSquad(squad, bubble.delta.setLength(200f))
                }
            }
        }

        // Process repulsor forces
        for (ship in repulsors) {
            ship.sector!!.shipTree!!.findInCircle(ship.transform.posGlobal, ship.template.repulsion!!.range)
                    .map { it.value!! }
                    .filter { !ship.team.isAlliedWith(it.team) }
                    .forEach { other ->
                        val r2 = ship.transform.posGlobal.dst2(other.transform.posGlobal)
                        val mag = ship.template.repulsion!!.force / r2
                        val force = other.transform.posGlobal.cpy().sub(ship.transform.posGlobal).setLength(mag).scl(SpaceAdmiral.DELTA_TIME)

                        other.velocity.mulAdd(force, 1 / other.template.mass)
                        ship.velocity.mulAdd(force, -1 / ship.template.mass)
                    }
        }

        // Move ships
        for (team in teams) {
            for (squad in team.squads) {
                for (ship in squad.ships) {
                    ship.updatePosition()
                }
            }
        }

        // Process projectiles
        // TODO

        // Bring out yer dead (ships)
        for (team in teams) {
            for (squad in team.squads) {
                for (ship in squad.ships) {
                    if (ship.health.isDead) {
                        ship.onDeath()
                        ship.sector!!.recentlyDiedShips.add(ship)
                    }
                }
            }
        }

        for (sector in sectors) {
            for (it in sector.recentlyDiedShips) {
                it.parent.ships.remove(it)
            }
        }

        // Bring out yer dead (squads)
        /*val deadSquads = mutableListOf<Squad>()
        gameState.squads.forEach { (_, squad) ->
            if (squad.isDead) {
                deadSquads.add(squad)
                squad.onDeath()
            }
        }
        deadSquads.forEach {
            gameState.squads.remove(it.uuid)
        }*/

        // Increment time
        time++
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(GameState::class.java)
    }

}
