package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.action.ActionCM
import xyz.plenglin.spaceadmiral.game.action.AttackSquadAction
import xyz.plenglin.spaceadmiral.game.action.MoveSquadAction
import xyz.plenglin.spaceadmiral.util.rect
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.sector.command.MoveToTransform
import xyz.plenglin.spaceadmiral.view.ui.sector.command.SquadCommandInputProcessor
import xyz.plenglin.spaceadmiral.view.ui.sector.selection.SquadSelectionInputProcessor
import java.util.*

class SelectedSquadHighlighter(
        private val sector: SectorCM,
        private val ui: GameUI,
        private val selector: SquadSelectionInputProcessor,
        private val input: SquadCommandInputProcessor) : RendererLayer {

    private val shape = ShapeRenderer()
    private lateinit var camera: OrthographicCamera

    override fun initialize(gameCamera: OrthographicCamera, uiCamera: OrthographicCamera) {
        this.camera = gameCamera
    }

    override fun render(delta: Float) {
        shape.projectionMatrix = camera.combined

        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        if (ui.selectedSquads.isNotEmpty()) {
            shape.color = COLOR_SELECTION
            shape.begin(ShapeRenderer.ShapeType.Filled)
            ui.selectedSquads.forEach { squad ->
                drawSelectedSquad(squad)
            }
            shape.end()
        }

        when (val state = input.state) {
            is MoveToTransform -> {
                if (state.dragged) {
                    shape.color = COLOR_COMMAND
                    shape.begin(ShapeRenderer.ShapeType.Filled)
                    state.generateDraggedTransform().forEach { _, st ->
                        val transforms = st.generateChildTransforms()
                        transforms.forEach { trs ->
                            val pos = trs.posGlobal
                            shape.circle(pos.x, pos.y, 0.5f, 10)
                        }
                    }
                    shape.end()
                }
            }
        }

        selector.state?.let { selectionState ->
            if (!selectionState.dragged) return@let
            val rect = selectionState.getSelectionBox().toGdxRect()

            shape.color = COLOR_SELECTION
            shape.begin(ShapeRenderer.ShapeType.Filled)

            selectionState.getSelectedSquads(sector.shipTree).forEach { squad ->
                shape.highlightSquad(squad)
            }

            shape.rect(rect)
            shape.end()

            shape.begin(ShapeRenderer.ShapeType.Line)
            shape.rect(rect)

            shape.end()
        }


        Gdx.gl.glDisable(GL20.GL_BLEND)

    }

    private fun drawSelectedSquad(squad: SquadCM) {
        shape.highlightSquad(squad)

        if (squad.queuedActions.isEmpty()) return

        val actions = LinkedList(squad.queuedActions)
        val state = actions.pop()
        var prev: ActionCM? = null
        actions.add(0, state)

        while (actions.isNotEmpty()) {
            val action = actions.pop()
            val start = prev?.endPos?.transform?.posGlobal ?: squad.centerOfMass
            when (action) {
                is MoveSquadAction.CM -> {
                    shape.color = COLOR_MOVE
                }
                is AttackSquadAction.CM -> {
                    shape.color = COLOR_ATTACK
                }
            }
            shape.line(start, action.endPos.transform.posGlobal)
            prev = action
        }
    }

    private fun ShapeRenderer.highlightSquad(squad: SquadCM) {
        squad.ships.forEach { ship ->
            val pos = ship.transform.posGlobal
            circle(pos.x, pos.y, squad.template.displayScale * 0.4f, 10)
        }
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        shape.dispose()
    }

    private companion object {
        @JvmStatic
        private val COLOR_SELECTION = Color(1f, 1f, 0f, 0.2f)
        @JvmStatic
        private val COLOR_COMMAND = Color(1f, 0f, 0f, 0.4f)
        @JvmStatic
        private val COLOR_ATTACK = Color(1f, 0f, 0f, 0.4f)
        @JvmStatic
        private val COLOR_MOVE = Color(0f, 1f, 1f, 0.4f)
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SelectedSquadHighlighter::class.java)
    }
}
