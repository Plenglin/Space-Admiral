package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Camera
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.GameStateTraverser

interface GameStateRenderer : GameStateTraverser {

    fun draw(gs: GameState, camera: Camera) {
        initializeDrawing()

        finalizeDrawing()
    }

    fun create(camera: Camera)

    fun initializeDrawing()
    fun finalizeDrawing()

}