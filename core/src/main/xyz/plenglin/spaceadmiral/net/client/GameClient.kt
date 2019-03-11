package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.team.Team
import java.lang.IllegalArgumentException
import java.util.*
import java.util.concurrent.locks.ReentrantLock

/**
 * The client, after initial handshake and initial data
 */
class GameClient(private val server: ServerInterface) {
    private val gsLock = ReentrantLock(true)
    var gameState: GameState? = null
    val team: UUID = server.clientTeam

    init {
        server.attachClient(this)
    }

    inline operator fun <reified T> get(uuid: UUID): Ref<T> = when (T::class.java) {
        Ship::class.java -> ShipRef(uuid, this) as Ref<T>
        Projectile::class.java -> ProjectileRef(uuid, this) as Ref<T>
        Team::class.java -> TeamRef(uuid, this) as Ref<T>
        Squad::class.java -> SquadRef(uuid, this) as Ref<T>
        else -> throw IllegalArgumentException("${T::class.java} is not supported!")
    }

    inline operator fun <reified T> invoke(uuid: UUID): T = when (T::class.java) {
        Ship::class.java -> gameState?.ships?.get(uuid) as T
        Projectile::class.java -> gameState?.projectiles?.get(uuid) as T
        Team::class.java -> gameState?.teams?.get(uuid) as T
        Squad::class.java -> gameState?.squads?.get(uuid) as T
        else -> throw IllegalArgumentException("${T::class.java} is not supported!")
    }

    fun update() {

    }
}

