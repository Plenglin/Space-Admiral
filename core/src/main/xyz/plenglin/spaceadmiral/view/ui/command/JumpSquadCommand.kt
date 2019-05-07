package xyz.plenglin.spaceadmiral.view.ui.command

import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.GameUI

object JumpSquadCommand : SquadCommand {
    override val displayName: String = "Warp Drive"

    override fun onActivate(ui: GameUI, targets: Set<SquadCM>, finishListener: (CommandResult) -> Unit): SquadCommandContext {
        return Context(ui, targets, finishListener)
    }

    private class Context(val ui: GameUI, val targets: Set<SquadCM>, val finishListener: (CommandResult) -> Unit) : SquadCommandContext {
        private var startingSector: SectorCM? = ui.sectorScreen?.sector

        init {
            ui.openGrid()
        }

        override fun cancel() {
            startingSector?.let { ui.openSector(it) }
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            val coord = ui.gridScreen.gridRenderer.getSectorAtScreenPos(screenX, screenY) ?: return false
            val sector = ui.client.getSector(coord)

            finishListener(Success)

            return false
        }
        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
        override fun keyTyped(character: Char): Boolean = false
        override fun scrolled(amount: Int): Boolean = false
        override fun keyUp(keycode: Int): Boolean = false
        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
        override fun keyDown(keycode: Int): Boolean = false
    }
}