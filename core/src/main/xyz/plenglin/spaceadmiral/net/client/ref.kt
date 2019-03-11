package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.team.Team
import java.util.*

sealed class Ref<T>(val uuid: UUID, val client: GameClient) {
    operator fun invoke(): T? = get
    abstract val get: T?
}

class SquadRef(uuid: UUID, client: GameClient) : Ref<Squad>(uuid, client) {
    override val get: Squad? get() = client.gameState?.squads?.get(uuid)
}

class TeamRef(uuid: UUID, client: GameClient) : Ref<Team>(uuid, client) {
    override val get: Team? get() = client.gameState?.teams?.get(uuid)
}

class ShipRef(uuid: UUID, client: GameClient) : Ref<Ship>(uuid, client) {
    override val get: Ship? get() = client.gameState?.ships?.get(uuid)
}

class ProjectileRef(uuid: UUID, client: GameClient) : Ref<Projectile>(uuid, client) {
    override val get: Projectile? get() = client.gameState?.projectiles?.get(uuid)
}

