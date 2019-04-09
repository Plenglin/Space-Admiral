package xyz.plenglin.spaceadmiral.view.model

import java.util.*

class GameStateVM {
    val squads = hashMapOf<UUID, SquadVM>()
    val sectors = hashMapOf<UUID, SquadVM>()
}