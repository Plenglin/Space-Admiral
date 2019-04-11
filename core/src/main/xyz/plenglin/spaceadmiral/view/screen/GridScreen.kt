package xyz.plenglin.spaceadmiral.view.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SpaceAdmiral
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.view.grid.GridRenderer
import xyz.plenglin.spaceadmiral.view.grid.SimpleGridRenderer
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.SmoothCameraInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.grid.SectorSelectionInputProcessor

class GridScreen(private val client: GameClient) : Screen {
    //private val batch: SpriteBatch = SpriteBatch()

    private var lastSectorScreen: SectorScreen? = null

    private val gameCamera: OrthographicCamera = OrthographicCamera().apply {
        zoom = 0.01f
    }
    private val uiCamera: OrthographicCamera = OrthographicCamera()

    private val gridRenderer: GridRenderer = SimpleGridRenderer()
    private val ui: GameUI = GameUI(client, uiCamera)

    private val inputCameraPosition = SmoothCameraInputProcessor(gameCamera)
    private val inputSectorSelection = SectorSelectionInputProcessor(this, client, gridRenderer)
    private val inputMultiplexer = InputMultiplexer(ui.stage, inputCameraPosition, inputSectorSelection)

    override fun show() {
        logger.info("showing GridScreen")

        lastSectorScreen?.let {
            logger.info("Disposing child {}", it)
            it.dispose()
        }

        Gdx.input.inputProcessor = inputMultiplexer

        gridRenderer.initialize(gameCamera, uiCamera)
    }

    override fun render(delta: Float) {
        logger.debug("performing a render, FPS = {}", 1 / delta)

        inputCameraPosition.update(delta)
        gameCamera.update()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val gs = client.gameState

        gridRenderer.draw(gs)
        ui.render(delta)
    }

    override fun resume() {
        logger.info("resuming")
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun pause() {
        logger.info("pausing")
        Gdx.input.inputProcessor = null
    }

    override fun hide() {
        logger.info("hiding")
        Gdx.input.inputProcessor = null
    }

    override fun resize(width: Int, height: Int) {
        logger.info("resizing to {} {}", width, height)
        inputCameraPosition.resize(width, height)
        gameCamera.setToOrtho(false, width.toFloat(), height.toFloat())
        uiCamera.setToOrtho(false, width.toFloat(), height.toFloat())
        uiCamera.update()
    }

    override fun dispose() {
        logger.info("disposing")
        gridRenderer.dispose()
        ui.dispose()
    }

    fun openSector(sector: SectorCM) {
        val screen = SectorScreen(this, client, sector)
        lastSectorScreen = screen
        SpaceAdmiral.screen = screen
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(GridScreen::class.java)
    }
}