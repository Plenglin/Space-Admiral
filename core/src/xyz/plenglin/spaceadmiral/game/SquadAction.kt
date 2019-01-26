package xyz.plenglin.spaceadmiral.game

sealed class SquadAction {

}

class Move(val target: Transform2D) : SquadAction()