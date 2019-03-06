package xyz.plenglin.spaceadmiral.view.screen

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.net.client.GameClient
import xyz.plenglin.spaceadmiral.view.renderer.GameStateRenderer
import xyz.plenglin.spaceadmiral.view.renderer.SimpleGameStateRenderer
import xyz.plenglin.spaceadmiral.view.ui.SmoothCameraControllerInput

class GameScreen(val client: GameClient) : Screen {
    lateinit var batch: SpriteBatch

    lateinit var gameCamera: OrthographicCamera
    lateinit var gameRenderer: GameStateRenderer

    lateinit var uiCamera: OrthographicCamera
    lateinit var uiStage: Stage

    private lateinit var cameraControllerInput: SmoothCameraControllerInput
    private val inputMultiplexer = InputMultiplexer()

    private val gameInstance = GameInstance()

    override fun show() {
        logger.info("showing GameScreen")

        batch = SpriteBatch()

        gameCamera = OrthographicCamera()
        gameRenderer = SimpleGameStateRenderer()

        gameCamera.position.set(0f, 0f, 0f)
        uiCamera = OrthographicCamera()
        uiStage = Stage()

        gameCamera.position.set(0f, 0f, 1f)
        gameRenderer.initialize(gameCamera, uiCamera)
        cameraControllerInput = SmoothCameraControllerInput(gameCamera)

        inputMultiplexer.addProcessor(cameraControllerInput)
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun render(delta: Float) {
        logger.debug("rendering GameScreen, FPS = {}", 1 / delta)
        uiStage.act(delta)

        client.update()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        cameraControllerInput.update(delta)

        client.gameState?.let {
            it.updateTrees()
            gameRenderer.draw(it)
        } ?: logger.warn("Did not receive a gamestate, not drawing anything!")

        /*
        batch.projectionMatrix = uiCamera.combined
        Texture((gameRenderer as SimpleGameStateRenderer).shipPixmap).let {
            batch.begin()
            batch.draw(it, 0f, 0f)
            batch.end()
            it.dispose()
        }

        gameRenderer.getShipAtPoint(Gdx.input.x, Gdx.input.y)?.let {
            logger.info("Mouse is currently hovering {} {} over ship {}", Gdx.input.x, Gdx.input.y, it)
        }
        */
        uiStage.draw()
    }

    override fun pause() {
        logger.info("Pausing")
    }

    override fun resume() {
        logger.info("Resuming")
    }

    override fun resize(width: Int, height: Int) {
        logger.info("Changing to a new resolution: {}x{}", width, height)
        cameraControllerInput.resize(width, height)
        uiCamera.setToOrtho(false, width.toFloat(), height.toFloat())
        gameRenderer.onResize(width, height)
    }

    override fun hide() {
        logger.info("Hiding")
    }

    override fun dispose() {
        logger.info("Disposing")
        batch.dispose()
    }

    companion object {
        const val cameraSpeed = 500f
        @JvmStatic
        val logger = LoggerFactory.getLogger(GameScreen::class.java)
    }
}