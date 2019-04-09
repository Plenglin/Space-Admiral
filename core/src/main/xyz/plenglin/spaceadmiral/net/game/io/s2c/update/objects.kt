package xyz.plenglin.spaceadmiral.net.game.io.s2c.update

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.Sector
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.Transform2D
import java.io.Serializable
import java.util.*

interface ActionDTO {
    val endPos: SquadTransform
}

data class ProjectileDTO internal constructor(
        val uuid: UUID,
        val pos: Vector2,
        val velocity: Vector2
)

data class SectorDTO internal constructor(
        val pos: IntVector2,
        val squads: List<SquadDTO>,
        val projectiles: List<ProjectileDTO>
) : Serializable

data class SquadDTO internal constructor(
        val uuid: UUID,
        val ships: List<ShipDTO>,
        val actions: List<ActionDTO>
) : Serializable

data class ShipDTO internal constructor(
        val uuid: UUID,
        val transform: Transform2D
)

fun Projectile.asDTO(): ProjectileDTO {
    return ProjectileDTO(
            uuid,
            pos,
            velocity
    )
}

fun Ship.asDTO(): ShipDTO {
    return ShipDTO(
            uuid,
            transform
    )
}

fun Squad.asDTO(): SquadDTO {
    return SquadDTO(
            uuid,
            ships.map { it.asDTO() },
            actionQueue.map { it.toDTO() }
    )
}

fun Sector.asDTO(): SectorDTO {
    return SectorDTO(
            pos,
            squads.map { (_, s) -> s.asDTO() },
            projectiles.map { (_, p) -> p.asDTO() }
    )
}
