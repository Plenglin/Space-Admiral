package xyz.plenglin.spaceadmiral.net.game.client

import xyz.plenglin.spaceadmiral.game.Sector
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.util.IntVector2
import java.util.*

sealed class Ref<T, U : Comparable<U>>(val id: U, val client: GameClient) : Comparable<Ref<T, U>> {
    operator fun invoke(): T? = getObject
    abstract val getObject: T?

    override fun hashCode() = id.hashCode()
    override fun compareTo(other: Ref<T, U>): Int {
        return id.compareTo(other.id)
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ref<*, *>

        if (id != other.id) return false
        if (client != other.client) return false
        if (getObject != other.getObject) return false

        return true
    }
}

class SquadRef(uuid: UUID, client: GameClient) : Ref<Squad, UUID>(uuid, client) {
    override val getObject: Squad? get() = client.gameState?.squads?.get(id)
    val parent: TeamRef? by lazy { getObject?.let { TeamRef(it.team.uuid, client) } }
}

fun Squad.toRef(client: GameClient) = SquadRef(uuid, client)

class TeamRef(uuid: UUID, client: GameClient) : Ref<Team, UUID>(uuid, client) {
    override val getObject: Team? get() = client.gameState?.teams?.get(id)
}

class ShipRef(uuid: UUID, client: GameClient) : Ref<Ship, UUID>(uuid, client) {
    override val getObject: Ship? get() = client.gameState?.ships?.get(id)
    val parent: SquadRef? by lazy { getObject?.let { SquadRef(it.parent.uuid, client) } }
}

class ProjectileRef(uuid: UUID, client: GameClient) : Ref<Projectile, UUID>(uuid, client) {
    override val getObject: Projectile? get() = client.gameState?.projectiles?.get(id)
    val parent: TeamRef? by lazy { getObject?.team?.let { TeamRef(it.uuid, client) } }
}

class SectorRef(pos: IntVector2, client: GameClient) : Ref<Sector, IntVector2>(pos, client) {
    override val getObject: Sector? get() = client.gameState?.getSector(id)
}