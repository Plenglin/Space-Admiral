package xyz.plenglin.spaceadmiral.util

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

fun Camera.unproject2(vec: Vector3) = unproject(vec).let { Vector2(it.x, it.y) }
fun Camera.unproject2(vec: Vector2) = unproject(Vector3(vec, 0f)).let { Vector2(it.x, it.y) }
fun Camera.unproject2(x: Float, y: Float) = unproject2(Vector2(x, y))

fun ShapeRenderer.rect(rect: Rectangle) {
    rect(rect.x, rect.y, rect.width, rect.height)
}
