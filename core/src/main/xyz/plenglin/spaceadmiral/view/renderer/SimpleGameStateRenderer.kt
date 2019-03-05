package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship

class SimpleGameStateRenderer : GameStateRenderer {

    private val shipTriangle = listOf(
            Vector2(1.0f, 0.0f),
            Vector2(0.0f, 0.5f),
            Vector2(0.0f, -0.5f)
    )
    lateinit var shape: ShapeRenderer
    lateinit var gameCamera: Camera
    lateinit var uiCamera: Camera

    override fun initialize(gameCamera: Camera, uiCamera: Camera) {
        shape = ShapeRenderer()
        this.gameCamera = gameCamera
        this.uiCamera = uiCamera
    }

    override fun draw(gs: GameState) {
        logger.trace("{} beginning drawing", this)
        shape.projectionMatrix = gameCamera.combined

        shape.begin(ShapeRenderer.ShapeType.Filled)
        shape.color = Color.WHITE
        shape.circle(0f, 0f, 0.1f)
        shape.end()

        shape.begin(ShapeRenderer.ShapeType.Line)

        shape.circle(0f, 0f, 100f)

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
        if (gameCamera.frustum.pointInFrustum(pos.x, pos.y, 0f)) {
            val transformed = shipTriangle.map {
                it.cpy().rotateRad(ship.transform.angleGlobal).add(pos)
            }
            shape.color = ship.parent.team.color
            shape.polygon(floatArrayOf(
                    transformed[0].x, transformed[0].y,
                    transformed[1].x, transformed[1].y,
                    transformed[2].x, transformed[2].y
                    ))
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

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SimpleGameStateRenderer::class.java)
    }

}