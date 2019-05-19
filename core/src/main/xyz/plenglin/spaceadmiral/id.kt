package xyz.plenglin.spaceadmiral

import xyz.plenglin.spaceadmiral.util.IntVector2
import java.util.*

typealias TeamID = UUID
fun nextTeamID(): WarpBubbleID = UUID.randomUUID()

typealias WarpBubbleID = UUID
fun nextWarpBubbleID(): WarpBubbleID = UUID.randomUUID()

typealias SectorID = IntVector2
//fun nextTeamID(): WarpBubbleID = UUID.randomUUID()

typealias ShipID = UUID
fun nextShipID(): ShipID = UUID.randomUUID()

typealias SquadID = UUID
fun nextSquadID(): SquadID = UUID.randomUUID()

typealias TurretID = UUID
fun nextTurretID(): TurretID = UUID.randomUUID()

typealias ProjectileID = UUID
fun nextProjectileID(): ProjectileID = UUID.randomUUID()
