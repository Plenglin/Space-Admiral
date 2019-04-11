package xyz.plenglin.spaceadmiral.view.ui.sector.selection

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.util.unproject2
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.renderer.SectorRenderer
import xyz.plenglin.spaceadmiral.view.ui.GameUI

class SquadSelectionInputProcessor(
        val sector: SectorCM,
        private val ui: GameUI,
        private val gameCamera: OrthographicCamera,
        private val client: GameClient,
        private val renderer: SectorRenderer)
    : InputProcessor {

    var state: SelectionState? = null

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button != Input.Buttons.LEFT) return false
        logger.info("Selection began at {} {}", screenX, screenY)

        if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            logger.info("Control was not held, clearing selection")
            ui.selectedSquads.clear()
        } else {
            logger.info("Control was held, will not clear selection")
        }

        state = SelectionState(ui.client.team, gameCamera.unproject2(screenX.toFloat(), screenY.toFloat()))
        return true
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        state?.let {
            it.dragged = true
            it.end.set(gameCamera.unproject2(screenX.toFloat(), screenY.toFloat()))
            logger.info("Selection dragged, state={}", it)
            return true
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button != Input.Buttons.LEFT) return false
        val state = state ?: return false
        this.state = null
        state.end.set(gameCamera.unproject2(screenX.toFloat(), screenY.toFloat()))
        logger.info("touchUp event with state={}", state)

        if (state.dragged) {
            logger.info("Selection was dragged, interpreting as selection box")
            val selected = state.getSelectedSquads(sector.shipTree).map { client.getSquad(it.uuid) }
            logger.debug("Selected ships {}", selected)
            ui.selectedSquads.addAll(selected)
        } else {
            val ship = renderer.getShipAtScreenPos(screenX, screenY)
            logger.info("Clicked at {} {}, corresponding to {}", screenX, screenY, ship)
            if (ship != null && ship.team.uuid == client.team) {
                val squad = ship.squad
                logger.info("Ship {} selected, corresponding to squad {}", ship, squad)
                logger.debug("Adding squad")
                ui.selectedSquads.add(squad)
            } else {
                logger.info("Clearing selection, no ships selected")
                ui.selectedSquads.clear()
                return false
            }
        }

        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun scrolled(amount: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false
    override fun keyDown(keycode: Int): Boolean = false

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SquadSelectionInputProcessor::class.java)
    }
}