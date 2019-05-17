package xyz.plenglin.spaceadmiral.view.ui.command

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.game.io.c2s.ClearSquadActionQueueCommand
import xyz.plenglin.spaceadmiral.net.game.io.c2s.WarpSquadCommand
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.SquadCommandController

object JumpSquadCommand : SquadCommand {

    @JvmStatic
    private val logger = LoggerFactory.getLogger(javaClass)

    override val displayName: String = "Warp Drive"

    override fun onActivate(ui: GameUI, recipients: Set<SquadCM>, controller: SquadCommandController): SquadCommandContext {
        return Context(ui, recipients, controller)
    }

    private class Context(val ui: GameUI, val recipients: Set<SquadCM>, val controller: SquadCommandController) : SquadCommandContext {
        private var startingSector: SectorCM? = ui.sectorScreen?.sector

        override fun initialize() {
            ui.openGrid()
        }

        override fun cancel() {
            logger.info("Canceling {}", this)
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            val coord = ui.gridScreen.gridRenderer.getSectorAtScreenPos(screenX, screenY) ?: return false
            logger.info("{} clicked on {}", this, coord)

            val client = ui.client

            recipients.forEach { squad ->
                logger.debug("{} sending to {}", this, squad)
                client.sendCommand(ClearSquadActionQueueCommand(squad.uuid))
            }
            client.sendCommand(WarpSquadCommand(recipients.map { it.uuid }, coord))

            controller.onCommandFinish(SquadCommandResult.Success)
            return true
        }

        override fun unshow() {
            startingSector?.let { ui.openSector(it) }
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