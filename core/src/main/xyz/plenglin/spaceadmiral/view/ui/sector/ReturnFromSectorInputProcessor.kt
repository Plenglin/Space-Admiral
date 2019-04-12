package xyz.plenglin.spaceadmiral.view.ui.sector

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import xyz.plenglin.spaceadmiral.view.ui.GameUI

class ReturnFromSectorInputProcessor(val ui: GameUI) : InputAdapter() {

    override fun keyDown(keycode: Int): Boolean {
        return when (keycode) {
            Input.Keys.ESCAPE -> {
                ui.openGrid()
                true
            }
            else -> false
        }
    }

}
