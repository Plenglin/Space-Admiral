package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.view.ui.command.SquadCommand
import xyz.plenglin.spaceadmiral.view.ui.command.SquadCommandContext


/**
 * Handles the context of giving squads actions.
 */
class SquadCommandController(val ui: GameUI) : InputProcessor {

    var currentCommand: SquadCommandContext? = null

    fun performSquadAction(action: SquadCommand) {
        logger.info("Cancelling {}", currentCommand)
        currentCommand?.cancel()

        logger.info("Creating context for {}", action)
        val context = action.onActivate(ui, ui.selectedSquads) { result ->
            logger.info("{} finished with {}", action, result)
        }
        currentCommand = context

        logger.debug("Initializing context {}", context)
        context.initialize()
    }

    fun cancel() {
        currentCommand?.cancel()
        currentCommand = null
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
            currentCommand?.touchUp(screenX, screenY, pointer, button) ?: false

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean =
            currentCommand?.mouseMoved(screenX, screenY) ?: false

    override fun keyTyped(character: Char): Boolean =
            currentCommand?.keyTyped(character) ?: false

    override fun scrolled(amount: Int): Boolean =
            currentCommand?.scrolled(amount) ?: false

    override fun keyUp(keycode: Int): Boolean =
            currentCommand?.keyUp(keycode) ?: false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean =
            currentCommand?.touchDragged(screenX, screenY, pointer) ?: false

    override fun keyDown(keycode: Int): Boolean {
        val command = currentCommand ?: return false

        return when (keycode) {
            Input.Keys.ESCAPE -> {
                cancel()
                true
            }
            else -> command.keyDown(keycode)
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean =
            currentCommand?.touchDown(screenX, screenY, pointer, button) ?: false

    private companion object {
        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(SquadCommandController::class.java)
    }

}