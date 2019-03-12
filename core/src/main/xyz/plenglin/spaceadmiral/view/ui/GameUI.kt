package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.net.client.GameClient
import xyz.plenglin.spaceadmiral.net.client.Ref
import xyz.plenglin.spaceadmiral.net.client.ShipRef
import xyz.plenglin.spaceadmiral.net.client.SquadRef

class GameUI(val client: GameClient, camera: OrthographicCamera) : Disposable {
    private val viewport = ScreenViewport(camera)
    val stage: Stage = Stage(viewport)

    val selectedSquads: MutableSet<SquadRef> = HashSet()

    init {

    }

    fun onShipSelected(ship: ShipRef?, ctrl: Boolean) {
        if (ship == null) {
            logger.info("Ship null selected, clearing squad selection")
            selectedSquads.clear()
            return
        }
        logger.info("Ship {} selected, corresponding to squad {}", ship, selectedSquads)
        if (!ctrl) {
            logger.debug("Control was not held, clearing selection")
            selectedSquads.clear()
        }
        logger.debug("Adding squad")
        selectedSquads.add(ship.parent!!)
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
