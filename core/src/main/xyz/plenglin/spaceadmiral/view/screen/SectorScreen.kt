package xyz.plenglin.spaceadmiral.view.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.renderer.SectorRenderer
import xyz.plenglin.spaceadmiral.view.renderer.SelectedSquadHighlighter
import xyz.plenglin.spaceadmiral.view.renderer.SimpleSectorRenderer
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.SmoothCameraInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.sector.ReturnFromSectorInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.sector.command.SquadCommandInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.sector.selection.SquadSelectionInputProcessor

class SectorScreen(
        private val client: GameClient,
        private val ui: GameUI,
        val sector: SectorCM) : Screen {

    private val batch: SpriteBatch = SpriteBatch()

    private val gameCamera: OrthographicCamera = OrthographicCamera().apply {
        zoom = .1f
    }

    private val gameRenderer: SectorRenderer = SimpleSectorRenderer()

    private val inputCameraPosition: SmoothCameraInputProcessor = SmoothCameraInputProcessor(gameCamera)
    private val inputSquadCommand = SquadCommandInputProcessor(ui, client, gameCamera, gameRenderer)
    private val inputSquadSelect = SquadSelectionInputProcessor(sector, ui, gameCamera, client, gameRenderer)
    private val inputReturnFromSector = ReturnFromSectorInputProcessor(ui)
    private val inputMultiplexer = InputMultiplexer(
            ui.stage,
            ui.squadCommand,
            inputCameraPosition,
            inputSquadCommand,
            inputSquadSelect,
            inputReturnFromSector
    )

    private val squadCommandHighlighter = SelectedSquadHighlighter(sector, ui, inputSquadSelect, inputSquadCommand)

    init {
        logger.info("Initializing SectorScreen")
        gameCamera.position.set(0f, 0f, 0f)
        gameCamera.setToOrtho(false)
    }

    override fun show() {
        logger.info("showing SectorScreen")

        gameRenderer.initialize(gameCamera, ui.camera)
        squadCommandHighlighter.initialize(gameCamera, ui.camera)

        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun render(delta: Float) {
        logger.trace("SectorScreen performing a render, FPS = {}", 1 / delta)

        for (squad in sector.squads) {
            if (!squad.visible) continue
            for (ship in squad.ships) {
                ship.updateRender(delta)
            }
        }

        sector.onRender()
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
        ui.viewport.update(width, height)
        gameRenderer.resize(width, height)
        ui.resize(width, height)
    }

    override fun dispose() {
        logger.info("Disposing")
        batch.dispose()
    }

    companion object {
        const val cameraSpeed = 500f
        @JvmStatic
        val logger = LoggerFactory.getLogger(SectorScreen::class.java)
    }
}