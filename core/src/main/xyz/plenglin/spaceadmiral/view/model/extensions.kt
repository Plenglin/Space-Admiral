package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.net.game.io.s2c.initial.ClientInitialPayload


fun ClientInitialPayload.toClientModel(): GameStateCM {
    val cmGameState = GameStateCM()
    val gs = gameState

    for (sector in gs.sectors) {
        cmGameState.sectorMap[sector.pos] = SectorCM(sector.pos, cmGameState)
    }

    gs.teams.forEach { gsTeam ->
        val color = Color()
        Color.argb8888ToColor(color, gsTeam.color)
        val uuid = gsTeam.uuid
        val cmTeam = TeamCM(uuid, cmGameState, color)
        cmGameState.teamMap[uuid] = cmTeam

        for (gsSquad in gsTeam.squads) {
            val cmSquadParentSector = cmGameState[gsSquad.sector!!.pos]!!
            val cmSquad = SquadCM(
                    gsSquad.uuid,
                    cmTeam,
                    gsSquad.template,
                    gsSquad.transform
            )

            cmSquad.sector = cmSquadParentSector
            cmTeam.squads.add(cmSquad)
            //cmSquadParentSector.squadMap[cmSquad.uuid] = cmSquad

            for (gsShip in gsSquad.ships) {
                val cmShip = cmSquad.addShip(
                        gsShip.uuid,
                        gsShip.transform.toGlobal()
                )

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
