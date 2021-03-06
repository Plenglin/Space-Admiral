package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Disposable
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.model.ShipCM

interface SectorRenderer : Disposable {
    fun initialize(gameCamera: OrthographicCamera, uiCamera: OrthographicCamera)
    fun draw(delta: Float, gs: SectorCM)
    fun resize(width: Int, height: Int)
    fun getShipAtScreenPos(x: Int, y: Int): ShipCM?
}