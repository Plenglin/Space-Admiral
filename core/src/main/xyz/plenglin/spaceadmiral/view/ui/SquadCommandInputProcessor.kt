package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.squad.MoveSquadAction
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.util.unproject2
import xyz.plenglin.spaceadmiral.view.renderer.GameStateRenderer

class SquadCommandInputProcessor(val ui: GameUI, val gameCamera: OrthographicCamera, val renderer: GameStateRenderer) : InputProcessor {

    var state: CommandState? = null

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val selectedSquad = ui.selectedSquads
        if (selectedSquad.isEmpty()) {
            logger.debug("No squads selected, ignoring input event")
            return false
        }

        when (button) {
            Input.Buttons.RIGHT -> {
                logger.debug("Received a right click at {} {}")

                // Perform an attack
                val target = renderer.getShipAtScreenPos(screenX, screenY)?.parent
                if (target != null && !selectedSquad.contains(target) && target.team != ui.client.team) {
                    logger.info("Creating an attack command for {} to attack {}", selectedSquad, target)
                    state = CommandState.Attack(ui.selectedSquads, target)
                    return true
                }

                // Perform a move
                logger.info("Creating a move command for {}", selectedSquad)
                val mousePos = gameCamera.unproject2(Vector2(screenX.toFloat(), screenY.toFloat()))
                state = CommandState.MoveToTransform(selectedSquad, mousePos)
                return true
            }
        }
        return false
    }
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        val state = state
        when (state) {
            is CommandState.MoveToTransform -> {
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
                it.actionQueue.clear()
            }
        }

        return when (state) {
            is CommandState.MoveToTransform -> {
                val target: SquadTransform = if (state.dragged) {
                    logger.info("Generating simple squad recipient transform")
                    //val newTransform = state.recipients.transform.transform.
                    val destination = gameCamera.unproject2(screenX.toFloat(), screenY.toFloat())
                } else {
                    logger.info("Generating simple squad recipient transform")
                    SquadTransform()
                }
                recipients.actionQueue.add(MoveSquadAction(recipients, target))
                true
            }
            is CommandState.Attack -> {
                val target = renderer.getShipAtScreenPos(screenX, screenY)?.parent
                if (state.target != target) {
                    logger.info("Cancelling {} because we ended on a different recipient", state)
                    return true
                }
                true
            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.ESCAPE -> {
                state ?: return false
                logger.info("Received ESCAPE, cancelling current action builder {}", state)
                this.state = null
                return true
            }
            Input.Keys.X -> {
                if (ui.selectedSquads.isEmpty()) {
                    logger.info("Cannot cancel if squads not selected")
                    return false
                }
                logger.info("Clearing action queue for {}", ui.selectedSquads)
                ui.selectedSquads.forEach {
                    it.actionQueue.clear()
                }
                return true
            }
        }

    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun scrolled(amount: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false

    sealed class CommandState {
        abstract val recipients: Set<Squad>

        data class MoveToTransform(override val recipients: Set<Squad>, val start: Vector2, var end: Vector2? = null, var dragged: Boolean = false) : CommandState()
        data class Attack(override val recipients: Set<Squad>, val target: Squad) : CommandState()
    }

    companion object {
        @JvmStatic private val logger = LoggerFactory.getLogger(SquadSelectionInputProcessor::class.java)

    }
}

