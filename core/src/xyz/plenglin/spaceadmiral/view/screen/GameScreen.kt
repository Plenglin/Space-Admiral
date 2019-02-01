package xyz.plenglin.spaceadmiral.view.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import xyz.plenglin.spaceadmiral.net.client.Client

class GameScreen(val client: Client) : Screen {
    lateinit var batch: SpriteBatch
    lateinit var img: Texture
    lateinit var gameCamera: OrthographicCamera

    override fun show() {
        gameCamera = OrthographicCamera()
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        gameCamera.update()
        batch.projectionMatrix = gameCamera.combined
        batch.begin()
        batch.draw(img, 0f, 0f)
        batch.end()
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun hide() {
    }

    override fun dispose() {
        batch.dispose()
        img.dispose()
    }

}