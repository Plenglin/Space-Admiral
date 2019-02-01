package xyz.plenglin.spaceadmiral.view.renderer

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.projectile.Projectile
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.squad.Squad

class SimpleGameStateRenderer : GameStateRenderer {

    private val shipTriangle = listOf(
            Vector2(2.0f, 0.0f),
            Vector2(0.0f, 1.0f),
            Vector2(0.0f, -1.0f)
    )
    lateinit var shape: ShapeRenderer
    lateinit var camera: Camera

    override fun create(camera: Camera) {
        shape = ShapeRenderer()
        this.camera = camera
    }

    override fun initializeDrawing() {
        shape.projectionMatrix = camera.combined
        shape.begin()
    }

    override fun finalizeDrawing() {
        shape.end()
    }

    override fun traverse(squad: Squad) {
        shape.color = squad.team.color
    }

    override fun traverse(ship: Ship) {
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

    override fun traverse(projectile: Projectile) {
        val pos = projectile.pos
        if (camera.frustum.pointInFrustum(pos.x, pos.y, 0f)) {
            shape.color = projectile.team.color
            shape.circle(pos.x, pos.y, 1f)
        }
    }

}