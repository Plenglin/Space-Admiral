package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.SpaceAdmiral.UPDATE_PERIOD
import xyz.plenglin.spaceadmiral.game.ship.Ship.Companion.IS_DEAD
import xyz.plenglin.spaceadmiral.game.squad.ShipType
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ShipUDTO
import xyz.plenglin.spaceadmiral.util.LinearInterpolator2
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.util.*

class ShipCM(val uuid: UUID, val squad: SquadCM, val transform: Transform2D) {

    val interpolator = LinearInterpolator2(UPDATE_PERIOD / 1000f)
    val velocity: Vector2 = Vector2()

    val team: TeamCM get() = squad.team
    val template: ShipType get() = squad.template
    val turrets = hashMapOf<UUID, TurretCM>()
    val gameState: GameStateCM get() = squad.gameState
    var alive = true

    fun updateWith(dto: ShipUDTO) {
        interpolator.pushActual(dto.transform.posGlobal)
        transform.set(dto.transform)
        velocity.set(dto.velocity)
        alive = (dto.flags and IS_DEAD) == 0
    }

    fun updateRender(delta: Float) {
        if (alive) {
            transform.setLocalPosition(interpolator.pushSample(delta))
        } else {
            transform.setLocalPosition(transform.posGlobal.cpy().mulAdd(velocity, delta))
        }
    }

}