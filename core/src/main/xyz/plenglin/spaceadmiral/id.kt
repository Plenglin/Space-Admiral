package xyz.plenglin.spaceadmiral

import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.random
import java.util.*

typealias TeamSubID = Int
typealias SquadSubID = Byte
typealias ShipSubID = Short
typealias TurretSubID = Byte

open class TeamID(open val team: TeamSubID) {
    override fun toString(): String {
        return "T-${rawString()}"
    }

    fun rawString(): String {
        return Integer.toHexString(team).capitalize()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TeamID

        if (team != other.team) return false

        return true
    }

    override fun hashCode(): Int {
        return team
    }
}
fun nextTeamID(): TeamID = TeamID(random.nextInt())


open class SquadID(val team: TeamID, val squad: SquadSubID) {
    override fun toString(): String {
        return "S-${rawString()}"
    }

    fun rawString(): String {
        return "${team.rawString()}-$squad"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SquadID

        if (team != other.team) return false
        if (squad != other.squad) return false

        return true
    }

    override fun hashCode(): Int {
        var result = team.hashCode()
        result = 31 * result + squad
        return result
    }
}

open class ShipID(val squad: SquadID, val ship: ShipSubID) {
    override fun toString(): String {
        return "H-${rawString()}"
    }

    fun rawString(): String {
        return "${squad.rawString()}-$ship"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShipID

        if (squad != other.squad) return false
        if (ship != other.ship) return false

        return true
    }

    override fun hashCode(): Int {
        var result = squad.hashCode()
        result = 31 * result + ship
        return result
    }
}

open class TurretID(val ship: ShipID, val turret: TurretSubID) {
    override fun toString(): String {
        return "M-${rawString()}"
    }

    fun rawString(): String {
        return "${ship.rawString()}-$turret"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TurretID

        if (ship != other.ship) return false
        if (turret != other.turret) return false

        return true
    }

    override fun hashCode(): Int {
        var result = ship.hashCode()
        result = 31 * result + turret
        return result
    }
}

typealias ProjectileID = UUID
fun nextProjectileID(): ProjectileID = UUID.randomUUID()

typealias WarpBubbleID = UUID
fun nextWarpBubbleID(): WarpBubbleID = UUID.randomUUID()

typealias SectorID = IntVector2
//fun nextTeamID(): WarpBubbleID = UUID.randomUUID()

