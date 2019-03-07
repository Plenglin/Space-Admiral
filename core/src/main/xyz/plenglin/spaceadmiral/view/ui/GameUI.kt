package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.net.client.GameClient

class GameUI(val client: GameClient, val camera: OrthographicCamera) : Disposable {
    private val viewport = ScreenViewport(camera)
    private val stage: Stage = Stage(viewport)

    var selectedSquad: Squad? = null

    init {

    }

    fun onShipSelected(ship: Ship?) {
        selectedSquad = ship?.parent
        logger.info("Ship {} selected, corresponding to squad {}", ship, selectedSquad)
    }

    fun render(delta: Float) {
        stage.act(delta)
        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun dispose() {
        stage.dispose()
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(GameUI::class.java)
    }

}
