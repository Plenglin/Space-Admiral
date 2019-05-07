package xyz.plenglin.spaceadmiral.view.ui.command

import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.GameUI

object MoveSquadCommand : SquadCommand {
    override val displayName: String = "Move"

    override fun onActivate(ui: GameUI, recipients: Set<SquadCM>, finishListener: (SquadCommandResult) -> Unit): SquadCommandContext {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}