package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import java.util.*
import kotlin.collections.HashSet

class SimpleGameStateRenderer : GameStateRenderer {

    private val shipTriangle = listOf(
            Vector2(1.0f, 0.0f),
            Vector2(0.0f, 0.5f),
            Vector2(0.0f, -0.5f)
    )
    lateinit var shape: ShapeRenderer
    lateinit var gameCamera: Camera
    lateinit var uiCamera: Camera
    var shipPixmap: Pixmap? = null

    private val pixelToShip = HashMap<Int, Ship>()
    private var nextShipColor = 1
    private var height: Int = 0

    override fun initialize(gameCamera: Camera, uiCamera: Camera) {
        shape = ShapeRenderer()
        this.gameCamera = gameCamera
        this.uiCamera = uiCamera
    }

    override fun draw(gs: GameState) {
        logger.trace("{} beginning drawing", this)

        nextShipColor = 0xff0000
        pixelToShip.clear()
        shipPixmap?.setColor(0)
        shipPixmap?.fill()

        shape.projectionMatrix = gameCamera.combined

        shape.begin(ShapeRenderer.ShapeType.Filled)
        shape.color = Color.WHITE
        shape.circle(0f, 0f, 0.1f)
        shape.end()

        shape.begin(ShapeRenderer.ShapeType.Line)

        gs.ships.forEach { _, ship ->
            draw(ship)
        }
        gs.projectiles.forEach { _, proj ->
            draw(proj)
        }
        shape.end()
    }

    private fun draw(ship: Ship) {
        logger.trace("rendering {}", ship)
        ship.transform.update()
        val pos = ship.transform.posGlobal
        val pos3d = Vector3(pos.x, pos.y, 0f)
        if (gameCamera.frustum.pointInFrustum(pos3d)) {
            // Draw the ship in the world
            val transformed = shipTriangle.map {
                it.cpy().rotateRad(ship.transform.angleGlobal).add(pos)
            }
            shape.color = ship.parent.team.color
            shape.polygon(floatArrayOf(
                    transformed[0].x, transformed[0].y,
                    transformed[1].x, transformed[1].y,
                    transformed[2].x, transformed[2].y
                    ))

            // Draw the ship on the clicking pixmap
            shipPixmap?.let {
                pixelToShip[nextShipColor] = ship
                val screenPos = gameCamera.project(pos3d)
                val radius = (gameCamera.combined.scaleX * 720).toInt()
                logger.debug("{} {}", radius, pos3d)
                val x = screenPos.x.toInt()
                val y = screenPos.y.toInt()
                it.setColor(nextShipColor)
                it.fillCircle(x, y, Math.max(MIN_SHIP_RADIUS, radius))
                nextShipColor += 1
            }
        }
    }

    private fun draw(projectile: Projectile) {
        logger.trace("rendering {}", projectile)
        val pos = projectile.pos
        if (gameCamera.frustum.pointInFrustum(pos.x, pos.y, 0f)) {
            shape.color = projectile.team?.color ?: Color.WHITE
            shape.circle(pos.x, pos.y, 1f)
        }
    }

    override fun onResize(width: Int, height: Int) {
        logger.info("Resized! Creating new ship pixmap of resolution {}x{}", width, height)
        shipPixmap?.dispose()
        shipPixmap = Pixmap(width, height, Pixmap.Format.RGBA8888).apply {
            blending = Pixmap.Blending.None
            filter = null
        }
        this.height = height
    }

    override fun dispose() {
        shipPixmap?.dispose()
    }

    override fun getShipAtPoint(x: Int, y: Int): Ship? {
        val pixel = shipPixmap?.getPixel(x, height - y) ?: return null
        logger.debug("Querying {} {}: {}", x, y, pixel)
        return pixelToShip[pixel]
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SimpleGameStateRenderer::class.java)

        const val MIN_SHIP_RADIUS = 5
    }

}