package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Disposable
import xyz.plenglin.spaceadmiral.game.Sector
import xyz.plenglin.spaceadmiral.game.ship.Ship

interface SectorRenderer : Disposable {
    fun initialize(gameCamera: OrthographicCamera, uiCamera: OrthographicCamera)
    fun draw(delta: Float, gs: Sector)
    fun resize(width: Int, height: Int)
    fun getShipAtScreenPos(x: Int, y: Int): Ship?
}