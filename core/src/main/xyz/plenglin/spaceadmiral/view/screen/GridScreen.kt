package xyz.plenglin.spaceadmiral.view.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.view.grid.GridRenderer
import xyz.plenglin.spaceadmiral.view.grid.SimpleGridRenderer
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.SmoothCameraInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.grid.SectorSelectionInputProcessor

class GridScreen(private val client: GameClient) : Screen {
    //private val batch: SpriteBatch = SpriteBatch()

    private val gameCamera: OrthographicCamera = OrthographicCamera()
    private val uiCamera: OrthographicCamera = OrthographicCamera()

    private val gridRenderer: GridRenderer = SimpleGridRenderer()
    private val ui: GameUI = GameUI(client, uiCamera)

    private val inputCameraPosition = SmoothCameraInputProcessor(gameCamera)
    private val inputSectorSelection = SectorSelectionInputProcessor(this, client, gridRenderer)
    private val inputMultiplexer = InputMultiplexer(ui.stage, inputCameraPosition, inputSectorSelection)

    init {
        inputCameraPosition.targetZoom = 0.01f
    }

    override fun hide() {

    }

    override fun show() {
        logger.info("showing GridScreen")
        Gdx.input.inputProcessor = inputMultiplexer

        //gameCamera.zoom = 100f
        gridRenderer.initialize(gameCamera, uiCamera)
        //squadCommandHighlighter.initialize(gameCamera, uiCamera)
    }

    override fun render(delta: Float) {
        inputCameraPosition.update(delta)
        gameCamera.update()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val gs = client.gameState
        if (gs == null) {
            logger.warn("No gamestate detected, not drawing anything!")
            return
        }
        val tadar = client.tadarData!!

        gridRenderer.draw(gs)
        ui.render(delta)
    }

    override fun resume() {
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun pause() {
        Gdx.input.inputProcessor = null
    }

    override fun resize(width: Int, height: Int) {
        inputCameraPosition.resize(width, height)
        gameCamera.setToOrtho(false, width.toFloat(), height.toFloat())
        uiCamera.setToOrtho(false, width.toFloat(), height.toFloat())
        uiCamera.update()
    }

    override fun dispose() {
        gridRenderer.dispose()
        ui.dispose()
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SectorScreen::class.java)
    }
}