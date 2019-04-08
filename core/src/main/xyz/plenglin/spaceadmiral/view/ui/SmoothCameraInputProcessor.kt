package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.util.getMousePos3
import xyz.plenglin.spaceadmiral.view.screen.SectorScreen

class SmoothCameraInputProcessor(
        val camera: OrthographicCamera,
        val dampening: Float = 0.9f,
        val zoomRate: Float = 1.5f)
    : InputProcessor {

    var targetZoom: Float = camera.zoom
    var targetPos: Vector3 = camera.position.cpy()

    private var dx: Int = 0
    private var dy: Int = 0

    private var width: Int = 0
    private var height: Int = 0

    override fun keyDown(keycode: Int): Boolean = when (keycode) {
        Input.Keys.W -> {
            dy += 1
            true
        }
        Input.Keys.S -> {
            dy -= 1
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
        val mouseDelta = camera.unproject(getMousePos3())
        logger.info("mouse pos unprojected {}", mouseDelta)
        if (amount > 0) {
            targetPos.sub(mouseDelta).scl(zoomRate).add(mouseDelta)
            targetZoom *= zoomRate
        } else {
            targetPos.sub(mouseDelta).scl(1 / zoomRate).add(mouseDelta)
            targetZoom /= zoomRate
        }
        return true
    }

    fun update(delta: Float) {
        val trs = Vector3(dx.toFloat(), dy.toFloat(), 0f).scl(SectorScreen.cameraSpeed * delta * camera.zoom)

        targetPos.add(trs)

        camera.zoom = dampening * camera.zoom + (1 - dampening) * targetZoom
        camera.position.scl(dampening).mulAdd(targetPos, 1 - dampening)
        //camera.zoom = targetCamera.zoom
        //camera.position.set(targetCamera.position)
    }

    fun resize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean = false

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(SmoothCameraInputProcessor::class.java)
    }
}