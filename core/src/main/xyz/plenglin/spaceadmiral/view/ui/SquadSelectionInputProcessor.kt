package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.InputProcessor
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.view.renderer.GameStateRenderer

class SquadSelectionInputProcessor(
        private val ui: GameUI,
        private val renderer: GameStateRenderer)
    : InputProcessor {

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val ship = renderer.getShipAtScreenPos(screenX, screenY)
        logger.debug("Clicked at {} {}, corresponding to {}", screenX, screenY, ship)
        if (ship != null && ui.selectedSquads == null) {
            ui.onShipSelected(ship)
            return true
        } else {
            ui.onShipSelected(null)
        }
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