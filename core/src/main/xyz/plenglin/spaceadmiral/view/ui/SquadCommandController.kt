package xyz.plenglin.spaceadmiral.view.ui

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.view.ui.command.SquadCommand
import xyz.plenglin.spaceadmiral.view.ui.command.SquadCommandContext


/**
 * Handles the context of giving squads actions.
 */
class SquadCommandController(val ui: GameUI) {
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

    private companion object {
        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(SquadCommandController::class.java)
    }

}