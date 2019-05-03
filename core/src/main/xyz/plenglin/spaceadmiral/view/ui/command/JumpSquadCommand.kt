package xyz.plenglin.spaceadmiral.view.ui.command

import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.GameUI

object JumpSquadCommand : SquadCommand {
    override val displayName: String = "Jump Drive"

    override fun onActivate(ui: GameUI, targets: Set<SquadCM>, finishListener: () -> Unit): SquadCommandContext {
        return Context(ui, targets, finishListener)
    }

    private class Context(ui: GameUI, targets: Set<SquadCM>, finishListener: () -> Unit) : SquadCommandContext {
        override fun cancel() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun keyTyped(character: Char): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun scrolled(amount: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun keyUp(keycode: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun keyDown(keycode: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}