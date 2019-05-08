package xyz.plenglin.spaceadmiral.view.ui.command

import com.badlogic.gdx.InputProcessor
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.GameUI

interface SquadCommand {
    val displayName: String
    fun onActivate(ui: GameUI, recipients: Set<SquadCM>, finishListener: (SquadCommandResult) -> Unit): SquadCommandContext
}

interface SquadCommandContext : InputProcessor {
    fun initialize()
    fun cancel()
}

sealed class SquadCommandResult {
    object Success : SquadCommandResult()
    object Failure : SquadCommandResult()
}

