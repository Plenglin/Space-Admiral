package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.scene2d.window
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.ASSET_GAMESPRITE_ATLAS
import xyz.plenglin.spaceadmiral.ASSET_SKIN
import xyz.plenglin.spaceadmiral.ASSET_UI_ATLAS
import xyz.plenglin.spaceadmiral.SpaceAdmiral
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.screen.GridScreen
import xyz.plenglin.spaceadmiral.view.screen.SectorScreen

class GameUI(val game: SpaceAdmiral, val assets: AssetManager, val client: GameClient, val gridScreen: GridScreen, val camera: OrthographicCamera) : Disposable {

    val skin: Skin = assets.get(ASSET_SKIN)
    val uiAtlas = assets.get(ASSET_UI_ATLAS)
    val gameAtlas = assets.get(ASSET_GAMESPRITE_ATLAS)

    val viewport = ScreenViewport(camera)
    val stage: Stage = Stage(viewport)
    var sectorScreen: SectorScreen? = null

    val squadCommand: SquadCommandController = SquadCommandController(this)

    val selectedSquads: MutableSet<SquadCM> = HashSet()

    val ctrlSquadSelection by lazy { SquadSelectionController(this) }
    val infoPaneController = SquadInfoPaneController(this)

    val windowSquadList = window(title = "Squads") {
        add(ctrlSquadSelection.squadListTable)
        pack()
    }

    val windowInfoPane = window(title = "Squad Info") {
        add(infoPaneController.table)
        pack()
    }

    init {
        stage.addActor(windowSquadList)
        stage.addActor(windowInfoPane)
    }

    fun firstShow() {
        ctrlSquadSelection.updateSquadListTable()
        windowSquadList.pack()
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

    fun openGrid() {
        game.screen = gridScreen
    }

    fun openSector(sector: SectorCM) {
        val screen = SectorScreen(client, this, sector)
        sectorScreen = screen
        game.screen = screen
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(GameUI::class.java)
    }

}
