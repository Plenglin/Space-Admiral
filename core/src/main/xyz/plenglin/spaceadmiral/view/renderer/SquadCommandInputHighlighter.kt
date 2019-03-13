package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import xyz.plenglin.spaceadmiral.view.ui.MoveToTransform
import xyz.plenglin.spaceadmiral.view.ui.SquadCommandInputProcessor

class SquadCommandInputHighlighter(private val input: SquadCommandInputProcessor) : RendererLayer {
    private val shape = ShapeRenderer()
    private lateinit var camera: OrthographicCamera

    override fun initialize(gameCamera: OrthographicCamera, uiCamera: OrthographicCamera) {
        this.camera = gameCamera
    }

    override fun render(delta: Float) {
        shape.projectionMatrix = camera.combined

        val state = input.state
        when (state) {
            is MoveToTransform -> {
                shape.color = Color.YELLOW
                shape.begin(ShapeRenderer.ShapeType.Filled)
                if (state.dragged) {
                    state.generateDraggedTransform().forEach { _, st ->
                        st.generateChildTransforms().forEach { trs ->
                            shape.circle(trs.posGlobal.x, trs.posGlobal.y, 0.5f)
                        }
                    }
                }
                shape.end()
            }
            else -> {

            }
        }
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        shape.dispose()
    }
}