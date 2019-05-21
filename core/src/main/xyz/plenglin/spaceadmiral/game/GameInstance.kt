package xyz.plenglin.spaceadmiral.game

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SpaceAdmiral.Companion.DELTA_TIME
import xyz.plenglin.spaceadmiral.game.ship.Ship
import java.io.Serializable

class GameInstance : Serializable {
    val gameState = GameState()

    fun update() {
        logger.trace("update {}", gameState.time)

        // Sector update
        gameState.sectors.forEach { sector ->
            sector.updateInitial()
        }

        val repulsors = mutableListOf<Ship>()

        for (team in gameState.teams) {
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

        for (bubble in gameState.warpBubbles.values) {
            if (bubble.hasArrived(gameState.time)) {
                val sector = gameState[bubble.endSector]

                logger.info("{} has arrived at {}", bubble, sector)
                gameState.warpBubbles.remove(bubble.uuid)

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
                        val force = other.transform.posGlobal.cpy().sub(ship.transform.posGlobal).setLength(mag).scl(DELTA_TIME)

                        other.velocity.mulAdd(force, 1 / other.template.mass)
                        ship.velocity.mulAdd(force, -1 / ship.template.mass)
                    }
        }

        // Move ships
        for (team in gameState.teams) {
            for (squad in team.squads) {
                for (ship in squad.ships) {
                    ship.updatePosition()
                }
            }
        }

        // Process projectiles
        // TODO

        // Bring out yer dead (ships)
        for (team in gameState.teams) {
            for (squad in team.squads) {
                for (ship in squad.ships) {
                    if (ship.health.isDead) {
                        ship.onDeath()
                        ship.sector!!.recentlyDiedShips.add(ship)
                    }
                }
            }
        }

        for (sector in gameState.sectors) {
            sector.recentlyDiedShips.forEach {
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
        gameState.time++
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(GameInstance::class.java)
    }
}