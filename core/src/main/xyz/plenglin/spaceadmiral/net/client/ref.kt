package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.team.Team
import java.util.*

sealed class Ref<T>(val uuid: UUID, val client: GameClient) : Comparable<Ref<T>> {
    operator fun invoke(): T? = getObject
    abstract val getObject: T?

    override fun hashCode() = uuid.hashCode()
    override fun compareTo(other: Ref<T>): Int {
        return uuid.compareTo(other.uuid)
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ref<*>

        if (uuid != other.uuid) return false
        if (client != other.client) return false
        if (getObject != other.getObject) return false

        return true
    }
}

class SquadRef(uuid: UUID, client: GameClient) : Ref<Squad>(uuid, client) {
    override val getObject: Squad? get() = client.gameState?.squads?.get(uuid)
    val parent: TeamRef? by lazy { getObject?.let { TeamRef(it.team.uuid, client) } }
}

fun Squad.toRef(client: GameClient) = SquadRef(uuid, client)

class TeamRef(uuid: UUID, client: GameClient) : Ref<Team>(uuid, client) {
    override val getObject: Team? get() = client.gameState?.teams?.get(uuid)
}

class ShipRef(uuid: UUID, client: GameClient) : Ref<Ship>(uuid, client) {
    override val getObject: Ship? get() = client.gameState?.ships?.get(uuid)
    val parent: SquadRef? by lazy { getObject?.let { SquadRef(it.parent.uuid, client) } }
}

class ProjectileRef(uuid: UUID, client: GameClient) : Ref<Projectile>(uuid, client) {
    override val getObject: Projectile? get() = client.gameState?.projectiles?.get(uuid)
    val parent: TeamRef? by lazy { getObject?.team?.let { TeamRef(it.uuid, client) } }
}

