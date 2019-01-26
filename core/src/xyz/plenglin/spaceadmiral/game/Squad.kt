package xyz.plenglin.spaceadmiral.game

import xyz.plenglin.spaceadmiral.game.ship.Ship
import java.util.*

class Squad {
    val ships = mutableListOf<Ship>()
    val actionQueue = LinkedList<SquadAction>()
}