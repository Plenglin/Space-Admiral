package xyz.plenglin.spaceadmiral.view.ui

import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.command.SquadCommandContext


/**
 * Handles the context of giving squads actions.
 */
class SquadCommandController(val squad: SquadCM) {

    var currentCommand: SquadCommandContext? = null


}