package xyz.plenglin.spaceadmiral.net.game.io

import xyz.plenglin.spaceadmiral.game.TadarData
import xyz.plenglin.spaceadmiral.net.game.io.dto.SectorDTO
import java.io.Serializable

data class ClientUpdatePayload(
        val sectors: List<SectorDTO>,
        val tadar: TadarData
) : Serializable
