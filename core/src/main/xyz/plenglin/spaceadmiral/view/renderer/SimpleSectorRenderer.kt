package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.ship.weapon.HitscanFiringEvent
import xyz.plenglin.spaceadmiral.view.model.ProjectileCM
import xyz.plenglin.spaceadmiral.view.model.SectorCM
import xyz.plenglin.spaceadmiral.view.model.ShipCM
import java.util.*

class SimpleSectorRenderer : SectorRenderer {

    private val shipTriangle = listOf(
            Vector2(0.25f, 0.0f),
            Vector2(-0.25f, 0.1f),
            Vector2(-0.25f, -0.1f)
    )
    lateinit var shape: ShapeRenderer
    lateinit var gameCamera: OrthographicCamera
    lateinit var uiCamera: OrthographicCamera
    var shipPixmap: Pixmap? = null

    private val pixelToShip = HashMap<Int, ShipCM>()
    private var nextShipColor = 1
    private var height: Int = 0
    private val lasers = mutableListOf<Laser>()
    private val color = Color()

    override fun initialize(gameCamera: OrthographicCamera, uiCamera: OrthographicCamera) {
        shape = ShapeRenderer()
        this.gameCamera = gameCamera
        this.uiCamera = uiCamera
    }

    override fun draw(delta: Float, gs: SectorCM) {
        val clippingPoints = gameCamera.frustum.planePoints
        val xs = clippingPoints.map { it.x }.toList()
        val ys = clippingPoints.map { it.y }.toList()
        val xMin = xs.min()!!
        val xMax = xs.max()!!
        val yMin = ys.min()!!
        val yMax = ys.max()!!
        logger.debug("{} drawing with limits: x=[{}, {}] y=[{}, {}]", this, xMin, xMax, yMin, yMax)

        nextShipColor = 0xff0000
        pixelToShip.clear()
        shipPixmap?.setColor(0)
        shipPixmap?.fill()

        shape.projectionMatrix = gameCamera.combined

        // Radial distance markers
        shape.begin(ShapeRenderer.ShapeType.Line)
        shape.color = RADIAL_DIST_COLOR
        listOf(50f, 100f, 150f, 200f, 250f, 300f, 350f, 400f).forEach {
            shape.circle(0f, 0f, it, 50)
        }
        shape.end()

        // Firing events
        gs.firingEvents.forEach {
            if (it is HitscanFiringEvent) {
                val target = gs.ships[it.target]!!
                lasers.add(Laser(it.mount.transform.posGlobal.cpy(), target.transform.posGlobal.cpy()))
            }
        }

        // Draw and update lasers
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shape.begin(ShapeRenderer.ShapeType.Line)
        lasers.forEach {
            draw(it)
            it.color.a -= 0.9f * delta
        }
        lasers.removeAll { it.color.a <= 0.02f }
        shape.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        // Ships and projectiles
        shape.begin(ShapeRenderer.ShapeType.Line)
        gs.shipTree.findInRect(xMin, xMax, yMin, yMax).forEach { (_, ship) ->
            draw(ship!!)
        }
        gs.projectileTree.findInRect(xMin, xMax, yMin, yMax).forEach { (_, proj) ->
            draw(proj!!)
        }
        shape.end()
    }

    private fun draw(laser: Laser) {
        shape.color = laser.color
        shape.line(laser.start, laser.end)
    }

    private fun draw(ship: ShipCM) {
        logger.trace("rendering {}", ship)
        ship.transform.update()
        val pos = ship.transform.posGlobal
        val pos3d = Vector3(pos.x, pos.y, 0f)
        val scale = ship.template.displayScale

        if (gameCamera.frustum.pointInFrustum(pos3d)) {
            // Draw the ship in the world
            val transformed = shipTriangle.map {
                it.cpy().scl(scale).rotateRad(ship.transform.angleGlobal).add(pos)
            }

            shape.color = ship.team.color
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
                val x = screenPos.x.toInt()
                val y = screenPos.y.toInt()
                it.setColor(nextShipColor)
                it.fillCircle(x, y, (Math.max(MIN_SHIP_CLICK_RADIUS, radius) * scale).toInt())
                nextShipColor += 1
            }
        }
    }

    private fun draw(projectile: ProjectileCM) {
        logger.trace("rendering {}", projectile)
        val pos = projectile.pos
        if (gameCamera.frustum.pointInFrustum(pos.x, pos.y, 0f)) {
            color.set(projectile.team.color)
            shape.color = color
            shape.circle(pos.x, pos.y, 1f)
        }
    }

    override fun resize(width: Int, height: Int) {
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

    override fun getShipAtScreenPos(x: Int, y: Int): ShipCM? {
        val pixel = shipPixmap?.getPixel(x, height - y) ?: return null
        logger.debug("Querying {} {}: {}", x, y, pixel)
        return pixelToShip[pixel]
    }

    private companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SimpleSectorRenderer::class.java)

        @JvmStatic
        val RADIAL_DIST_COLOR = Color(0.2f, 0.2f, 0.2f, 1f)

        const val MIN_SHIP_CLICK_RADIUS = 5

        data class Laser(val start: Vector2, val end: Vector2, val color: Color = Color(0f, 1f, 1f, 0.2f))
    }

}
