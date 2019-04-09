package xyz.plenglin.spaceadmiral.net.game.io.s2c.update

import xyz.plenglin.spaceadmiral.game.TadarData
import java.io.Serializable

data class ClientUpdatePayload(
        val sectors: List<SectorUDTO>,
        val tadar: TadarData
) : Serializable
