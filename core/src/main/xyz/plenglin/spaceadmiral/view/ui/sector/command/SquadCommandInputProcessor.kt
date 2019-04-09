package xyz.plenglin.spaceadmiral.view.ui.sector.command

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.net.game.io.c2s.AttackSquadCommand
import xyz.plenglin.spaceadmiral.net.game.io.c2s.ClearSquadActionQueueCommand
import xyz.plenglin.spaceadmiral.net.game.io.c2s.MoveSquadCommand
import xyz.plenglin.spaceadmiral.util.unproject2
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.renderer.SectorRenderer
import xyz.plenglin.spaceadmiral.view.ui.GameUI

class SquadCommandInputProcessor(
        val ui: GameUI,
        val client: GameClient,
        val gameCamera: OrthographicCamera,
        val renderer: SectorRenderer) : InputProcessor {

    var state: CommandState? = null

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val recipients = ui.selectedSquads

        val target = renderer.getShipAtScreenPos(screenX, screenY)?.squad

        when (button) {
            Input.Buttons.RIGHT -> {
                logger.debug("Received a right click at {} {}. Potential recipients {}", recipients)

                if (recipients.isEmpty()) {
                    logger.debug("No squads are selected, deferring input event")
                    return false
                }

                if (target != null && !target.team.isAlliedWith(ui.client.team)) {
                    logger.info("Creating an attack command for {} to attack {}", recipients, target)
                    state = Attack(ui.selectedSquads, target)
                    return true
                }

                // Perform a move
                logger.info("Creating a move command for {}", recipients)
                val mousePos = gameCamera.unproject2(Vector2(screenX.toFloat(), screenY.toFloat()))
                state = MoveToTransform(recipients, mousePos)

                return true
            }
        }
        return false
    }
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val state = state
        when (state) {
            is MoveToTransform -> {
                logger.info("Dragging {}, position {} {}", state, screenX, screenY)
                state.end = gameCamera.unproject2(screenX.toFloat(), screenY.toFloat())

                state.dragged = state.end.cpy().sub(state.start).len2() > 10
                return true
            }
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val state = state ?: return false

        logger.info("Finalizing state {}", state)
        this.state = null
        val recipients = state.recipients

        return when (state) {
            is MoveToTransform -> {
                state.end = gameCamera.unproject2(screenX.toFloat(), screenY.toFloat())
                val targets = if (state.dragged) {
                    logger.info("Generating dragged squad recipient transform")
                    state.generateDraggedTransform()
                } else {
                    logger.info("Generating simple squad recipient transform")
                    state.generateSimpleTransform()
                }

                clearActionQueue(recipients)
                targets.forEach { (squad, end) ->
                    client.sendCommand(MoveSquadCommand(squad.uuid, end))
                }
                true
            }
            is Attack -> {
                val target = renderer.getShipAtScreenPos(screenX, screenY)?.squad
                if (state.target != target) {
                    logger.info("Cancelling {} because we ended on a different target", state)
                    return true
                }
                clearActionQueue(recipients)
                recipients.forEach {
                    client.sendCommand(AttackSquadCommand(it.uuid, target.uuid))
                }
                true
            }
        }
    }

    private fun clearActionQueue(recipients: Set<SquadCM>) {
        if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            logger.info("Clearing action queue of {} (shift not held)", recipients)
            recipients.forEach {
                client.sendCommand(ClearSquadActionQueueCommand(it.uuid))
            }
        } else {
            logger.info("Will not clear action queues of {} (shift held)", recipients)
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.ESCAPE -> {
                state ?: return false
                logger.info("Received CLEAR SELECTION, cancelling current action builder {}", state)
                this.state = null
                return true
            }
            Input.Keys.H -> {
                if (ui.selectedSquads.isEmpty()) {
                    logger.debug("Received CLEAR QUEUE, no squads selected, deferring this event")
                    return false
                }
                logger.info("Received HALT, clearing action queue for {}", ui.selectedSquads)
                ui.selectedSquads.forEach {
                    client.sendCommand(ClearSquadActionQueueCommand(it.uuid))
                }
                return true
            }
        }
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun scrolled(amount: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false

    companion object {
        @JvmStatic private val logger = LoggerFactory.getLogger(SquadCommandInputProcessor::class.java)
    }
}

