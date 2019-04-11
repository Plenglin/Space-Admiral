package xyz.plenglin.spaceadmiral.view.ui.sector

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import xyz.plenglin.spaceadmiral.view.screen.SectorScreen

class ReturnFromSectorInputProcessor(val sectorScreen: SectorScreen) : InputAdapter() {

    override fun keyDown(keycode: Int): Boolean {
        return when (keycode) {
            Input.Keys.ESCAPE -> {
                sectorScreen.returnToParent()
                true
            }
            else -> false
        }
    }

}
