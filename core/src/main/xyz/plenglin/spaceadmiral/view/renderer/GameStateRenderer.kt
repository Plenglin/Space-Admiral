package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Camera
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.data.GameStateDTO

interface GameStateRenderer {
    fun initialize(gameCamera: Camera, uiCamera: Camera)
    fun draw(gs: GameStateDTO)
}