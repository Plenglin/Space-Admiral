package xyz.plenglin.spaceadmiral.game.projectile

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.team.Team
import java.io.Serializable
import java.util.*

class Projectile(val team: Team, val pos: Vector2, val velocity: Vector2) : Serializable {
    val uuid: UUID = UUID.randomUUID()
    var hasDelta = false

    fun updateInitial() {
        hasDelta = false
    }

}