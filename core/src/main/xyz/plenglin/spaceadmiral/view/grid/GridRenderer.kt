package xyz.plenglin.spaceadmiral.view.grid

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Disposable
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.TadarData
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.view.model.GameStateCM

interface GridRenderer : Disposable {
    fun initialize(gameCamera: OrthographicCamera, uiCamera: OrthographicCamera)
    fun draw(gs: GameStateCM)
    fun resize(width: Int, height: Int)
    fun getSectorAtScreenPos(x: Int, y: Int): IntVector2?
}