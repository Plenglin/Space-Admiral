package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.utils.Disposable
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.ship.Ship

interface GameStateRenderer : Disposable {
    fun initialize(gameCamera: Camera, uiCamera: Camera)
    fun draw(gs: GameState)
    fun onResize(width: Int, height: Int)
    fun getShipAtPoint(x: Int, y: Int): Ship?
}