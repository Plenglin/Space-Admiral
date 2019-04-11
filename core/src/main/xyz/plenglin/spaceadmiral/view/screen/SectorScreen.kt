package xyz.plenglin.spaceadmiral.view.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.SpaceAdmiral
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.renderer.SectorRenderer
import xyz.plenglin.spaceadmiral.view.renderer.SimpleSectorRenderer
import xyz.plenglin.spaceadmiral.view.renderer.SquadCommandInputHighlighter
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.SmoothCameraInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.sector.ReturnFromSectorInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.sector.command.SquadCommandInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.sector.selection.SquadSelectionInputProcessor

class SectorScreen(
        private val parent: GridScreen,
        private val client: GameClient,
        private val sector: SectorCM) : Screen {

    private val batch: SpriteBatch = SpriteBatch()

    private val gameCamera: OrthographicCamera = OrthographicCamera()
    private val uiCamera: OrthographicCamera = OrthographicCamera()

    private val gameRenderer: SectorRenderer = SimpleSectorRenderer()

    private val ui: GameUI = GameUI(client, uiCamera)

    private val inputCameraPosition: SmoothCameraInputProcessor = SmoothCameraInputProcessor(gameCamera)
    private val inputSquadCommand = SquadCommandInputProcessor(ui, client, gameCamera, gameRenderer)
    private val inputSquadSelect = SquadSelectionInputProcessor(sector, ui, gameCamera, client, gameRenderer)
    private val inputReturnFromSector = ReturnFromSectorInputProcessor(this)
    private val inputMultiplexer = InputMultiplexer(
            ui.stage,
            inputCameraPosition,
            inputSquadCommand,
            inputSquadSelect,
            inputReturnFromSector
    )

    private val squadCommandHighlighter = SquadCommandInputHighlighter(sector, ui, client, inputSquadSelect, inputSquadCommand)

    init {
        logger.info("Initializing SectorScreen")
        gameCamera.position.set(0f, 0f, 0f)
        gameCamera.setToOrtho(false)
    }

    override fun show() {
        logger.info("showing SectorScreen")

        gameRenderer.initialize(gameCamera, uiCamera)
        squadCommandHighlighter.initialize(gameCamera, uiCamera)

        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun render(delta: Float) {
        logger.debug("SectorScreen performing a render, FPS = {}", 1 / delta)

        sector.updateTreesIfNecessary()
        client.update()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        inputCameraPosition.update(delta)
        gameCamera.update()

        squadCommandHighlighter.render(delta)
        gameRenderer.draw(delta, sector)
        ui.render(delta)
    }

    override fun resume() {
        logger.info("Resuming")
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun pause() {
        logger.info("Pausing")
        Gdx.input.inputProcessor = null
    }

    override fun hide() {
        logger.info("Hiding")
        Gdx.input.inputProcessor = null
    }

    override fun resize(width: Int, height: Int) {
        logger.info("Changing to a new resolution: {}x{}", width, height)
        inputCameraPosition.resize(width, height)
        squadCommandHighlighter.resize(width, height)

        gameCamera.setToOrtho(false, width.toFloat(), height.toFloat())
        uiCamera.setToOrtho(false, width.toFloat(), height.toFloat())
        gameRenderer.resize(width, height)
        ui.resize(width, height)
    }

    override fun dispose() {
        logger.info("Disposing")
        batch.dispose()
        ui.dispose()
    }

    fun returnToParent() {
        SpaceAdmiral.screen = parent
    }

    companion object {
        const val cameraSpeed = 500f
        @JvmStatic
        val logger = LoggerFactory.getLogger(SectorScreen::class.java)
    }
}