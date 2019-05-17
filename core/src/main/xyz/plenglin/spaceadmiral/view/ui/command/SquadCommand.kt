package xyz.plenglin.spaceadmiral.view.ui.command

import com.badlogic.gdx.InputProcessor
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.SquadCommandController

interface SquadCommand {
    val displayName: String
    fun onActivate(ui: GameUI, recipients: Set<SquadCM>, controller: SquadCommandController): SquadCommandContext
}

interface SquadCommandContext : InputProcessor {
    fun initialize()
    fun cancel()
    fun unshow()
}

sealed class SquadCommandResult {
    object Success : SquadCommandResult()
    object Failure : SquadCommandResult()
}

