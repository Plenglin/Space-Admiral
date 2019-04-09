package xyz.plenglin.spaceadmiral.net.game.io.s2c.update

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.Sector
import xyz.plenglin.spaceadmiral.game.TadarData
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.ship.weapon.FiringEvent
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.io.Serializable
import java.util.*


data class ClientUpdatePayload(
        val sectors: List<SectorUDTO>,
        val tadar: TadarData
) : Serializable

interface ActionDTO : Serializable {
    val endPos: SquadTransform
}

data class ProjectileUDTO internal constructor(
        val uuid: UUID,
        val pos: Vector2,
        val velocity: Vector2
) : Serializable

data class SectorUDTO internal constructor(
        val pos: IntVector2,
        val squads: List<SquadUDTO>,
        val projectiles: List<ProjectileUDTO>,
        val firingEvents: List<FiringEvent>
) : Serializable

data class SquadUDTO internal constructor(
        val uuid: UUID,
        val ships: List<ShipUDTO>,
        val actions: List<ActionDTO>
) : Serializable

data class ShipUDTO internal constructor(
        val uuid: UUID,
        val transform: Transform2D
) : Serializable

fun Projectile.asUpdateDTO(): ProjectileUDTO {
    return ProjectileUDTO(
            uuid,
            pos,
            velocity
    )
}

fun Ship.asUpdateDTO(): ShipUDTO {
    return ShipUDTO(
            uuid,
            transform
    )
}

fun Squad.asUpdateDTO(): SquadUDTO {
    return SquadUDTO(
            uuid,
            ships.map { it.asUpdateDTO() },
            actionQueue.map { it.toDTO() }
    )
}

fun Sector.asUpdateDTO(): SectorUDTO {
    return SectorUDTO(
            pos,
            squads.map { (_, s) -> s.asUpdateDTO() },
            projectiles.map { (_, p) -> p.asUpdateDTO() },
            firingEvents
    )
}
