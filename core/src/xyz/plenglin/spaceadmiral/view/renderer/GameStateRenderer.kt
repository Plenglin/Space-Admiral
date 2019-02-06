package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Camera
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.GameStateTraverser

interface GameStateRenderer : GameStateTraverser {

    fun draw(gs: GameState) {
        beginDrawing()

        endDrawing()
    }

    fun initialize(camera: Camera)

    fun beginDrawing()
    fun endDrawing()

}