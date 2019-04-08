package xyz.plenglin.spaceadmiral.net.game.io

import xyz.plenglin.spaceadmiral.game.TadarData
import java.io.Serializable

data class ClientUpdatePayload(
        val gameStateBytes: ByteArray,
        val tadar: TadarData
) : Serializable
