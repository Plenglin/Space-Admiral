package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.SpaceAdmiral.UPDATE_PERIOD
import xyz.plenglin.spaceadmiral.game.ship.ShipType
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ShipUDTO
import xyz.plenglin.spaceadmiral.util.LinearInterpolator2
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.util.*

class ShipCM(val uuid: UUID, val squad: SquadCM, val transform: Transform2D) {

    val interpolator = LinearInterpolator2(UPDATE_PERIOD / 1000f)
    val velocity: Vector2 = Vector2()

    private val interpV0: Vector2 = Vector2()
    private val interpV1: Vector2 = Vector2()

    val team: TeamCM get() = squad.team
    val template: ShipType get() = squad.template
    val turrets = hashMapOf<UUID, TurretCM>()
    val gameState: GameStateCM get() = squad.gameState

    fun updateWith(dto: ShipUDTO) {
        interpolator.pushActual(dto.transform.posGlobal)
        transform.set(dto.transform)
        velocity.set(dto.velocity)
    }

    fun updateDisplayTransform(delta: Float) {
        transform.setLocalPosition(interpolator.pushSample(delta))
    }

}