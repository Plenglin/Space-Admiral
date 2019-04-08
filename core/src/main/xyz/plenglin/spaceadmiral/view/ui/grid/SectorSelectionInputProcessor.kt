package xyz.plenglin.spaceadmiral.view.ui.grid

import com.badlogic.gdx.InputProcessor
import xyz.plenglin.spaceadmiral.SpaceAdmiral
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.view.grid.GridRenderer
import xyz.plenglin.spaceadmiral.view.screen.GridScreen
import xyz.plenglin.spaceadmiral.view.screen.SectorScreen

class SectorSelectionInputProcessor(val parent: GridScreen, val client: GameClient, val renderer: GridRenderer) : InputProcessor {

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val pos = renderer.getSectorAtScreenPos(screenX, screenY) ?: return false
        val sector = client.getSector(pos)
        SpaceAdmiral.screen = SectorScreen(parent, client, sector)
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

}