package xyz.plenglin.spaceadmiral.net.game.io.s2c.update

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.TadarData
import xyz.plenglin.spaceadmiral.game.action.ActionDTO
import xyz.plenglin.spaceadmiral.game.action.SquadAction
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.sector.Sector
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.Ship.Companion.IS_DEAD
import xyz.plenglin.spaceadmiral.game.ship.weapon.FiringEvent
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.squad.WarpBubble
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.ShipUDTO.Companion.SENT_FLAGS
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.io.Serializable
import java.util.*


data class ClientUpdatePayload(
        val sectors: List<SectorUDTO>,
        val warping: List<WarpBubbleUDTO>,
        val events: List<SectorEventDTO>,
        val tadar: TadarData
) : Serializable

data class WarpBubbleUDTO internal constructor(
        val squads: List<SquadUDTO>,
        val pos: Vector2,
        val velocity: Vector2
)

fun WarpBubble.asUpdateDTO(t: Long): WarpBubbleUDTO {
    return WarpBubbleUDTO(squads.map { it.asUpdateDTO() }, getPos(t), Vector2(0f, 0f))
}

data class ProjectileUDTO internal constructor(
        val uuid: UUID,
        val pos: Vector2,
        val velocity: Vector2
) : Serializable

fun Projectile.asUpdateDTO(): ProjectileUDTO {
    return ProjectileUDTO(
            uuid,
            pos,
            velocity
    )
}

data class SectorUDTO internal constructor(
        val pos: IntVector2,
        val squads: List<SquadUDTO>,
        val projectiles: List<ProjectileUDTO>,
        val firingEvents: List<FiringEvent>,
        val recentlyDiedShips: List<UUID>
) : Serializable

fun Sector.asUpdateDTO(): SectorUDTO {
    return SectorUDTO(
            pos,
            squads.map { (_, s) -> s.asUpdateDTO() },
            projectiles.map { (_, p) -> p.asUpdateDTO() },
            firingEvents,
            recentlyDiedShips.map { it.uuid }
    )
}

data class SquadUDTO internal constructor(
        val uuid: UUID,
        val ships: List<ShipUDTO>,
        val actions: List<ActionDTO>
) : Serializable

fun Squad.asUpdateDTO(): SquadUDTO {
    val actions = mutableListOf<ActionDTO>()
    stateScheduler.currentState?.let { actions.add((it as SquadAction).toDTO()) }
    actionQueue.forEach { actions.add(it.toDTO()) }
    return SquadUDTO(
            uuid,
            ships.filter { it.flags and IS_DEAD == 0 }
                    .map { it.asUpdateDTO() },
            actions
    )
}

data class ShipUDTO internal constructor(
        val uuid: UUID,
        val transform: Transform2D,
        val velocity: Vector2,
        val flags: Int
) : Serializable {
    companion object {
        /**
         * The flags to send over to the client.
         */
        const val SENT_FLAGS = IS_DEAD
    }
}

fun Ship.asUpdateDTO(): ShipUDTO {
    return ShipUDTO(
            uuid,
            transform,
            velocity,
            flags and SENT_FLAGS
    )
}
