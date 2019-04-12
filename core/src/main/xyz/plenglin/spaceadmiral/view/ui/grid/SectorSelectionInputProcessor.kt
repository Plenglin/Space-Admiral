package xyz.plenglin.spaceadmiral.view.ui.grid

import com.badlogic.gdx.InputProcessor
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.view.grid.GridRenderer
import xyz.plenglin.spaceadmiral.view.ui.GameUI

class SectorSelectionInputProcessor(
        private val ui: GameUI,
        private val client: GameClient,
        private val renderer: GridRenderer) : InputProcessor {

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val pos = renderer.getSectorAtScreenPos(screenX, screenY) ?: return false
        val sector = client.getSector(pos) ?: return false
        ui.openSector(sector)
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun scrolled(amount: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
    override fun keyDown(keycode: Int): Boolean = false
}