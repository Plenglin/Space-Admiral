package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.MoveToTransform
import xyz.plenglin.spaceadmiral.view.ui.SquadCommandInputProcessor

class SquadCommandInputHighlighter(private val ui: GameUI, private val input: SquadCommandInputProcessor) : RendererLayer {
    private val shape = ShapeRenderer()
    private lateinit var camera: OrthographicCamera

    override fun initialize(gameCamera: OrthographicCamera, uiCamera: OrthographicCamera) {
        this.camera = gameCamera
    }

    override fun render(delta: Float) {
        shape.projectionMatrix = camera.combined


        if (ui.selectedSquads.isNotEmpty()) {
            shape.color = Color.YELLOW
            shape.begin(ShapeRenderer.ShapeType.Filled)
            ui.selectedSquads.forEach { squad ->
                val pos = squad()!!.transform.generateChildTransforms().forEach { trs ->
                    val pos = trs.posGlobal
                    shape.circle(pos.x, pos.y, 0.5f)
                }
            }
            shape.end()
        }

        val state = input.state
        when (state) {
            is MoveToTransform -> {
                if (state.dragged) {
                    shape.color = Color.RED
                    shape.begin(ShapeRenderer.ShapeType.Filled)
                    state.generateDraggedTransform().forEach { _, st ->
                        val transforms = st.generateChildTransforms()
                        transforms.forEach { trs ->
                            val pos = trs.posGlobal
                            shape.circle(pos.x, pos.y, 0.5f)
                        }
                    }
                    shape.end()
                }
            }
        }

    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        shape.dispose()
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SquadCommandInputHighlighter::class.java)
    }
}