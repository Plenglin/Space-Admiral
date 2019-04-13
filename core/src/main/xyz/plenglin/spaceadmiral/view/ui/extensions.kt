package xyz.plenglin.spaceadmiral.view.ui

import xyz.plenglin.spaceadmiral.game.squad.ShipClassification

fun ShipClassification.getIconLabel(): String = when (this) {
    ShipClassification.CORVETTE -> "icon-corvette"
    ShipClassification.FIGHTER -> "icon-fighter"
    ShipClassification.CRUISER -> "icon-cruiser"
    else -> TODO()
}
