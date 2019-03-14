package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.client.GameClient
import xyz.plenglin.spaceadmiral.net.client.toRef
import xyz.plenglin.spaceadmiral.view.renderer.GameStateRenderer

class SquadSelectionInputProcessor(
        private val ui: GameUI,
        private val client: GameClient,
        private val renderer: GameStateRenderer)
    : InputProcessor {

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button != Input.Buttons.LEFT) return false

        val ship = renderer.getShipAtScreenPos(screenX, screenY)
        logger.debug("Clicked at {} {}, corresponding to {}", screenX, screenY, ship)
        if (ship != null) {
            val squad = ship.parent
            logger.info("Ship {} selected, corresponding to squad {}", ship, squad)
            if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                logger.debug("Control was not held, clearing selection")
                ui.selectedSquads.clear()
            }
            logger.debug("Adding squad")
            ui.selectedSquads.add(squad.toRef(client))
            return true
        }

        logger.info("Clearing selection")
        ui.selectedSquads.clear()
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun scrolled(amount: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
    override fun keyDown(keycode: Int): Boolean = false

    companion object {
        @JvmStatic private val logger = LoggerFactory.getLogger(SquadSelectionInputProcessor::class.java)
    }
}