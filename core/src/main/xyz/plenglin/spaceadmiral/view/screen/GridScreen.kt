package xyz.plenglin.spaceadmiral.view.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.view.ui.GameUI

class GridScreen(private val client: GameClient) : Screen {
    private val batch: SpriteBatch = SpriteBatch()

    private val gameCamera: OrthographicCamera = OrthographicCamera()
    private val uiCamera: OrthographicCamera = OrthographicCamera()

    private val ui: GameUI = GameUI(client, uiCamera)

    private val inputMultiplexer = InputMultiplexer(ui.stage)

    override fun hide() {

    }

    override fun show() {
    }

    override fun render(delta: Float) {
    }

    override fun resume() {
        Gdx.input.inputProcessor = inputMultiplexer
    }

    override fun pause() {
        Gdx.input.inputProcessor = null
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
    }

    companion object {
        const val cameraSpeed = 500f
        @JvmStatic
        val logger = LoggerFactory.getLogger(SectorScreen::class.java)
    }
}