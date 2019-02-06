package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Camera
import xyz.plenglin.spaceadmiral.game.GameState

interface GameStateRenderer {
    fun initialize(camera: Camera)
    fun draw(gs: GameState)
}