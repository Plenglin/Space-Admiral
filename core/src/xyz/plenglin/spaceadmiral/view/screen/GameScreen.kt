package xyz.plenglin.spaceadmiral.view.screen

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.client.Client
import xyz.plenglin.spaceadmiral.view.renderer.GameStateRenderer
import xyz.plenglin.spaceadmiral.view.renderer.SimpleGameStateRenderer

class GameScreen(val client: Client) : Screen {
    lateinit var batch: SpriteBatch

    lateinit var gameCamera: OrthographicCamera
    lateinit var uiCamera: OrthographicCamera
    lateinit var gameRenderer: GameStateRenderer

    private val gameWorldInput = GameInput()
    private val inputMultiplexer = InputMultiplexer()

    inner class GameInput : InputAdapter() {
        var dx: Int = 0
        var dy: Int = 0

        override fun keyDown(keycode: Int): Boolean = when (keycode) {
            Input.Keys.W -> {
                dy -= 1
                true
            }
            Input.Keys.S -> {
                dy += 1
                true
            }
            Input.Keys.A -> {
                dx -= 1
                true
            }
            Input.Keys.D -> {
                dx += 1
                true
            }
            else -> false
        }

        override fun keyUp(keycode: Int): Boolean  = when (keycode) {
            Input.Keys.W -> {
                dy = 0
                true
            }
            Input.Keys.S -> {
                dy = 0
                true
            }
            Input.Keys.A -> {
                dx = 0
                true
            }
            Input.Keys.D -> {
                dx = 0
                true
            }
            else -> false
        }

        override fun scrolled(amount: Int): Boolean {
            if (amount > 0) {
                gameCamera.zoom *= 1.5f
            } else {
                gameCamera.zoom /= 1.5f
            }
            return true
        }
    }

    override fun show() {
        gameCamera = OrthographicCamera()
        uiCamera = OrthographicCamera()
        batch = SpriteBatch()
        gameRenderer = SimpleGameStateRenderer()
        gameRenderer.initialize(gameCamera)

        inputMultiplexer.addProcessor(gameWorldInput)
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        println(gameCamera.position)

        gameCamera.translate(Vector2(gameWorldInput.dx.toFloat(), gameWorldInput.dy.toFloat()).scl(cameraSpeed * delta))
        gameCamera.update()

        gameRenderer.draw(client.gameState)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        gameCamera.setToOrtho(false)
        uiCamera.setToOrtho(false, width.toFloat(), height.toFloat())
    }

    override fun hide() {
    }

    override fun dispose() {
        batch.dispose()
    }

    companion object {
        const val cameraSpeed = 10f
        @JvmStatic
        val logger = LoggerFactory.getLogger(GameScreen::class.java)
    }
}