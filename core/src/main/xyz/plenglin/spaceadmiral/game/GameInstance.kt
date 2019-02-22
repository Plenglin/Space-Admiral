package xyz.plenglin.spaceadmiral.game

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.game.data.GameStateDTO
import xyz.plenglin.spaceadmiral.game.data.ProjectileDTO
import xyz.plenglin.spaceadmiral.game.data.ShipDTO
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.util.AdjustableClock
import xyz.plenglin.spaceadmiral.util.EventLoop
import xyz.plenglin.spaceadmiral.util.KDTree2
import java.util.*

class GameInstance {
    val teams = mutableListOf<Team>()
    val clock = AdjustableClock()
    val loop = EventLoop(clock)

    var shipTree = KDTree2<Ship>()
    var projTree = KDTree2<Projectile>()

    private val shipDTOTree = KDTree2<ShipDTO>()
    private val projDTOTree = KDTree2<ProjectileDTO>()

    fun createTeam(color: Color): Team {
        val out = Team(this, color)
        teams.add(out)
        return out
    }

    fun update() {
        val shipList = ArrayList<Ship>()
        val projList = ArrayList<Projectile>()

        teams.forEach { team ->
            team.squads.forEach { squad ->
                squad.update()
                squad.ships.forEach {
                    it.updateInitial()
                    shipList.add(it)
                }
            }
            team.projectiles.forEach { proj ->
                proj.updateInitial()
                projList.add(proj)
            }
        }

        shipList.shuffle()
        shipList.forEach {
            shipTree.insert(it.transform.posGlobal, it)
            shipDTOTree.insert(it.transform.posGlobal, it.createDTO())
        }
    }

    fun updateWith(dto: GameStateDTO) {
    }

    fun createDTO(): GameStateDTO {
        return GameStateDTO(shipDTOTree, projDTOTree)
    }
}