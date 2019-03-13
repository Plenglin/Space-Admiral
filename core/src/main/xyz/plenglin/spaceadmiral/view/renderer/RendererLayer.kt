package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Disposable

interface RendererLayer : Disposable {
    fun initialize(gameCamera: OrthographicCamera, uiCamera: OrthographicCamera)
    fun render(delta: Float)
    fun resize(width: Int, height: Int)
}