package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Camera
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.GameStateTraverser

interface GameStateRenderer {
    fun draw(gs: GameState)
    fun initialize(camera: Camera)
}