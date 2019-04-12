package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.scene2d.button
import ktx.scene2d.label
import ktx.scene2d.table
import ktx.scene2d.window
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SpaceAdmiral
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.model.ShipCM
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.screen.GridScreen
import xyz.plenglin.spaceadmiral.view.screen.SectorScreen

class GameUI(val client: GameClient, val gridScreen: GridScreen, val camera: OrthographicCamera) : Disposable {
    val viewport = ScreenViewport(camera)
    val stage: Stage = Stage(viewport)

    val selectedSquads: MutableSet<SquadCM> = HashSet()

    val squadList = window(title = "Squads") {
        table {
            client.gameState.squads.forEach { _, squad ->
                button {
                    label(squad.template.displayName)
                    addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {

                        }
                    })
                }
                row()
            }
        }
        pack()
    }

    init {
        stage.addActor(squadList)
    }

    fun onShipSelected(ship: ShipCM?, ctrl: Boolean) {
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
        selectedSquads.add(ship.squad)
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

    var sectorScreen: SectorScreen? = null

    fun openGrid() {
        SpaceAdmiral.screen = gridScreen
    }

    fun openSector(sector: SectorCM) {
        val screen = SectorScreen(client, this, sector)
        sectorScreen = screen
        SpaceAdmiral.screen = screen
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(GameUI::class.java)
    }

}
