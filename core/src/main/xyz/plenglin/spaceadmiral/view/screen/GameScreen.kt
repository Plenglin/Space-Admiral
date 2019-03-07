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
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.SmoothCameraInputProcessor

class GameScreen(val client: GameClient) : Screen {
    val batch: SpriteBatch = SpriteBatch()

    val gameCamera: OrthographicCamera = OrthographicCamera()
    val gameRenderer: GameStateRenderer = SimpleGameStateRenderer()
    val uiCamera: OrthographicCamera = OrthographicCamera()

    val ui: GameUI = GameUI(client, uiCamera)

    private val inputMultiplexer = InputMultiplexer()
    private val cameraController: SmoothCameraInputProcessor = SmoothCameraInputProcessor(ui, uiCamera)

    override fun show() {
        logger.info("showing GameScreen")

        gameCamera.position.set(0f, 0f, 0f)

        gameCamera.position.set(0f, 0f, 1f)
        gameRenderer.initialize(gameCamera, uiCamera)

        inputMultiplexer.addProcessor(cameraController)
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun render(delta: Float) {
        logger.debug("rendering GameScreen, FPS = {}", 1 / delta)

        client.update()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        cameraController.update(delta)

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
        cameraController.resize(width, height)
        uiCamera.setToOrtho(false, width.toFloat(), height.toFloat())
        uiCamera.update()
        gameRenderer.onResize(width, height)
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