package xyz.plenglin.spaceadmiral.game

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SpaceAdmiral.DELTA_TIME
import xyz.plenglin.spaceadmiral.game.ship.Ship
import java.io.Serializable

class GameInstance : Serializable {
    val gameState = GameState()

    fun update() {
        logger.debug("update {}", gameState.time)

        // Sector update
        gameState.sectors.forEach { (_, sector) ->
            sector.updateInitial()
        }

        // Squad update
        gameState.squads.forEach { (_, squad) ->
            squad.update()
        }

        // Ship initial logic update
        val repulsors = mutableListOf<Ship>()
        gameState.ships.forEach { (_, ship) ->
            ship.updateLogic()
            if (ship.template.repulsion != null) {
                repulsors.add(ship)
            }
        }

        // Process repulsor forces
        repulsors.forEach { ship ->
            ship.sector.shipTree!!.findInCircle(ship.transform.posGlobal, ship.template.repulsion!!.range)
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
        gameState.ships.forEach { (_, ship) ->
            ship.updatePosition()
        }

        // Process projectiles
        // TODO

        // Bring out yer dead (ships)
        gameState.ships.values.forEach { ship ->
            if (ship.health.isDead) {
                ship.onDeath()
                ship.sector.recentlyDiedShips.add(ship)
            }
        }
        gameState.sectors.values.forEach { sector ->
            sector.recentlyDiedShips.forEach {
                it.parent.ships.remove(it)
                gameState.ships.remove(it.uuid)
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