package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.scene2d.window
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SpaceAdmiral
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.screen.GridScreen
import xyz.plenglin.spaceadmiral.view.screen.SectorScreen

class GameUI(val client: GameClient, val gridScreen: GridScreen, val camera: OrthographicCamera) : Disposable {
    val viewport = ScreenViewport(camera)
    val stage: Stage = Stage(viewport)

    val selectedSquads: MutableSet<SquadCM> = HashSet()

    val ctrlSquadSelection = SquadSelectionController(this)

    val windowSquadList = window(title = "Squads") {
        add(ctrlSquadSelection.squadListTable)
        pack()
    }

    init {
        stage.addActor(windowSquadList)
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
