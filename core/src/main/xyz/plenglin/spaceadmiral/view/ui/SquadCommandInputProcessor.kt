package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.client.GameClient
import xyz.plenglin.spaceadmiral.net.client.toRef
import xyz.plenglin.spaceadmiral.net.io.ClearSquadActionQueueCommand
import xyz.plenglin.spaceadmiral.net.io.MoveSquadCommand
import xyz.plenglin.spaceadmiral.util.unproject2
import xyz.plenglin.spaceadmiral.view.renderer.GameStateRenderer

class SquadCommandInputProcessor(
        val ui: GameUI,
        val client: GameClient,
        val gameCamera: OrthographicCamera,
        val renderer: GameStateRenderer) : InputProcessor {

    var state: CommandState? = null

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val recipients = ui.selectedSquads
        if (recipients.isEmpty()) {
            logger.debug("No squads selected, deferring input event")
            return false
        }

        val target = renderer.getShipAtScreenPos(screenX, screenY)?.parent

        when (button) {
            Input.Buttons.LEFT -> {
                logger.debug("Received a left click at {} {}")
                if (target == null) {
                    logger.info("Did not actually click on a ship, deferring input event")
                    return false
                }
                if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    logger.info("Shift not pressed, clearing selection")
                    ui.selectedSquads.clear()
                }
                logger.info("Adding {} to selection", target)
                ui.selectedSquads.add(target.toRef(client))
                return true
            }
            Input.Buttons.RIGHT -> {
                logger.debug("Received a right click at {} {}")

                if (recipients.isEmpty()) {
                    logger.debug("No squads are selected, deferring input event")
                    return false
                }

                if (target != null && target.team.isAlliedWith(ui.client.team)) {
                    logger.info("Creating an attack command for {} to attack {}", recipients, target)
                    state = Attack(ui.selectedSquads, target.toRef(client))
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
                //logger.debug("Creating a move command for {}", selectedSquad)
                state.dragged = true
                state.end = Vector2(screenX.toFloat(), screenY.toFloat())
                return true
            }
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val state = state ?: return false

        logger.debug("Finalizing state {}", state)
        this.state = null
        val recipients = state.recipients

        if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            logger.info("Clearing action queue of {} (shift not held)", recipients)
            recipients.forEach {
                client.sendCommand(ClearSquadActionQueueCommand(it.uuid))
            }
        }

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

                targets.forEach { (squad, end) ->
                    client.sendCommand(MoveSquadCommand(squad.uuid, end))
                }
                true
            }
            is Attack -> {
                val target = renderer.getShipAtScreenPos(screenX, screenY)?.parent?.toRef(client)
                if (state.target != target) {
                    logger.info("Cancelling {} because we ended on a different target", state)
                    return true
                }
                // TODO client.sendCommand(Att)
                true
            }
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

