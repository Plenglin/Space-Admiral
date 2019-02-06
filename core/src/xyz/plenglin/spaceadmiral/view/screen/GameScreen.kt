package xyz.plenglin.spaceadmiral.view.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.net.client.Client
import xyz.plenglin.spaceadmiral.view.renderer.GameStateRenderer
import xyz.plenglin.spaceadmiral.view.renderer.SimpleGameStateRenderer

class GameScreen(val client: Client) : Screen {
    lateinit var batch: SpriteBatch
    lateinit var img: Texture

    lateinit var gameCamera: OrthographicCamera
    lateinit var uiCamera: OrthographicCamera
    lateinit var gameRenderer: GameStateRenderer

    val input = GameInput()

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
                dy -= 1
                true
            }
            Input.Keys.D -> {
                dy += 1
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
                dy = 0
                true
            }
            Input.Keys.D -> {
                dy = 0
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
        img = Texture("badlogic.jpg")
        gameRenderer = SimpleGameStateRenderer()
        gameRenderer.initialize(gameCamera)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        gameCamera.translate(Vector2(input.dx.toFloat(), input.dy.toFloat()).scl(cameraSpeed))
        gameCamera.update()

        gameRenderer.draw(client.gameState)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
        uiCamera.setToOrtho(false, width.toFloat(), height.toFloat())
    }

    override fun hide() {
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }

    companion object {
        const val cameraSpeed = 10f
    }
}