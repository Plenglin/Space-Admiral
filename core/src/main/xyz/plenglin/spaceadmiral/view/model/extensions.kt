package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.graphics.Color
import xyz.plenglin.spaceadmiral.net.game.io.s2c.initial.ClientInitialPayload


fun ClientInitialPayload.toClientModel(): GameStateCM {
    val out = GameStateCM()
    val gs = gameState

    gs.sectors.forEach { pos, sector ->
        out.sectors[pos] = SectorCM(pos, out)
    }

    gs.teams.forEach { uuid, gsTeam ->
        val color = Color()
        Color.argb8888ToColor(color, gsTeam.color)
        val cmTeam = TeamCM(uuid, out, color)
        out.teams[uuid] = cmTeam

        for (gsSquad in gsTeam.squads) {
            val cmSquad = SquadCM(
                    gsSquad.uuid,
                    cmTeam,
                    gsSquad.template,
                    gsSquad.transform
            )
            out.squads[gsSquad.uuid] = cmSquad

            for (gsShip in gsSquad.ships) {
                val cmShip = ShipCM(
                        gsShip.uuid,
                        cmSquad,
                        gsShip.transform.toGlobal()
                )
                out.ships[gsShip.uuid] = cmShip

                for (gsTurret in gsShip.turrets) {
                    val cmTurret = TurretCM(
                            gsTurret.uuid,
                            cmShip,
                            gsShip.transform.clone().apply { parent = cmShip.transform }
                    )
                    out.turrets[gsTurret.uuid] = cmTurret
                }
            }
        }
    }

    return out
}