package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.net.game.io.s2c.initial.ClientInitialPayload


fun ClientInitialPayload.toClientModel(): GameStateCM {
    val cmGameState = GameStateCM()
    val gs = gameState

    gs.sectors.forEach { (pos, sector) ->
        cmGameState.sectors[pos] = SectorCM(pos, cmGameState)
    }

    gs.teams.forEach { (uuid, gsTeam) ->
        val color = Color()
        Color.argb8888ToColor(color, gsTeam.color)
        val cmTeam = TeamCM(uuid, cmGameState, color)
        cmGameState.teams[uuid] = cmTeam

        for (gsSquad in gsTeam.squads) {
            val cmSquadParentSector = cmGameState.sectors.getValue(gsSquad.sector.pos)
            val cmSquad = SquadCM(
                    gsSquad.uuid,
                    cmTeam,
                    gsSquad.template,
                    gsSquad.transform
            )

            cmSquad.sector = cmSquadParentSector

            cmTeam.squads.add(cmSquad)
            cmGameState.squads[cmSquad.uuid] = cmSquad
            cmSquadParentSector.squads[cmSquad.uuid] = cmSquad

            for (gsShip in gsSquad.ships) {
                val cmShip = ShipCM(
                        gsShip.uuid,
                        cmSquad,
                        gsShip.transform.toGlobal()
                )
                cmSquad.ships[cmShip.uuid] = cmShip
                cmGameState.ships[cmShip.uuid] = cmShip
                cmSquadParentSector.ships[cmShip.uuid] = cmShip

                for (gsTurret in gsShip.turrets) {
                    val cmTurret = TurretCM(
                            gsTurret.uuid,
                            cmShip,
                            gsShip.transform.clone().apply { parent = cmShip.transform }
                    )
                    cmSquadParentSector.turrets[cmTurret.uuid] = cmTurret
                    cmShip.turrets[gsTurret.uuid] = cmTurret
                    //cmGameState.turrets[gsTurret.uuid] = cmTurret
                }
            }
        }
    }

    return cmGameState
}
