package xyz.plenglin.spaceadmiral.view.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.client.GameClient
import xyz.plenglin.spaceadmiral.view.renderer.GameStateRenderer
import xyz.plenglin.spaceadmiral.view.renderer.SimpleGameStateRenderer
import xyz.plenglin.spaceadmiral.view.renderer.SquadCommandInputHighlighter
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.SmoothCameraInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.command.SquadCommandInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.selection.SquadSelectionInputProcessor

class GameScreen(private val client: GameClient) : Screen {
    private val batch: SpriteBatch = SpriteBatch()

    private val gameCamera: OrthographicCamera = OrthographicCamera()
    private val uiCamera: OrthographicCamera = OrthographicCamera()

    private val gameRenderer: GameStateRenderer = SimpleGameStateRenderer()

    private val ui: GameUI = GameUI(client, uiCamera)

    private val inputCameraPosition: SmoothCameraInputProcessor = SmoothCameraInputProcessor(gameCamera)
    private val inputSquadCommand = SquadCommandInputProcessor(ui, client, gameCamera, gameRenderer)
    private val inputSquadSelect = SquadSelectionInputProcessor(ui, gameCamera, client, gameRenderer)
    private val inputMultiplexer = InputMultiplexer(ui.stage, inputCameraPosition, inputSquadCommand, inputSquadSelect)

    private val squadCommandHighlighter = SquadCommandInputHighlighter(ui, inputSquadCommand)

    init {
        logger.info("Initializing GameScreen")
        gameCamera.position.set(0f, 0f, 0f)
        gameCamera.setToOrtho(false)
    }

    override fun show() {
        logger.info("showing GameScreen")
        Gdx.input.inputProcessor = inputMultiplexer

        gameRenderer.initialize(gameCamera, uiCamera)
        squadCommandHighlighter.initialize(gameCamera, uiCamera)
    }

    override fun render(delta: Float) {
        logger.debug("GameScreen performing a render, FPS = {}", 1 / delta)

        client.update()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        inputCameraPosition.update(delta)
        gameCamera.update()

        squadCommandHighlighter.render(delta)
        client.gameState?.let {
            it.updateTrees()
            gameRenderer.draw(it)
        } ?: logger.warn("Did not receive a game state from client, not drawing anything!")

        ui.render(delta)
    }

    override fun pause() {
        logger.info("Pausing")
    }

    override fun resume() {
        logger.info("Resuming")
    }

    override fun resize(width: Int, height: Int) {
        logger.info("Changing to a new resolution: {}x{}", width, height)
        inputCameraPosition.resize(width, height)
        squadCommandHighlighter.resize(width, height)

        //uiCamera.setToOrtho(false, width.toFloat(), height.toFloat())
        gameRenderer.resize(width, height)
        ui.resize(width, height)
    }

    override fun hide() {
        logger.info("Hiding")
    }

    override fun dispose() {
        logger.info("Disposing")
        batch.dispose()
        ui.dispose()
        Gdx.input.inputProcessor = null
    }

    companion object {
        const val cameraSpeed = 500f
        @JvmStatic
        val logger = LoggerFactory.getLogger(GameScreen::class.java)
    }
}