package xyz.plenglin.spaceadmiral.view.ui.command

import com.badlogic.gdx.InputProcessor
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.GameUI

interface SquadCommand {
    val displayName: String
    fun onActivate(ui: GameUI, targets: Set<SquadCM>, finishListener: () -> Unit): SquadCommandContext
}

interface SquadCommandContext : InputProcessor {
    fun cancel()
}

