package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad

class SimpleGameStateRenderer : GameStateRenderer {

    private val shipTriangle = listOf(
            Vector2(1.0f, 0.0f),
            Vector2(0.0f, 0.5f),
            Vector2(0.0f, -0.5f)
    )
    lateinit var shape: ShapeRenderer
    lateinit var camera: Camera

    override fun initialize(camera: Camera) {
        shape = ShapeRenderer()
        this.camera = camera
    }

    override fun draw(gs: GameState) {
        logger.trace("{} beginning drawing", this)
        shape.projectionMatrix = camera.combined

        shape.begin(ShapeRenderer.ShapeType.Line)

        gs.teams.forEach { team ->
            shape.color = team.color
            team.squads.forEach { squad ->
                squad.ships.forEach(this@SimpleGameStateRenderer::draw)
            }
            team.projectiles.forEach(this@SimpleGameStateRenderer::draw)
        }

        shape.end()
    }

    private fun draw(ship: Ship) {
        ship.transform.update()
        val pos = ship.transform.posGlobal
        if (camera.frustum.pointInFrustum(pos.x, pos.y, 0f)) {
            val transformed = shipTriangle.map {
                it.cpy().rotate(ship.transform.angleGlobal).add(pos.x, pos.y)
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
        val pos = projectile.pos
        if (camera.frustum.pointInFrustum(pos.x, pos.y, 0f)) {
            shape.color = projectile.team.color
            shape.circle(pos.x, pos.y, 1f)
        }
    }

    companion object {
        @JvmStatic
        private val logger = LoggerFactory.getLogger(SimpleGameStateRenderer::class.java)
    }

}