package xyz.plenglin.spaceadmiral.util

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
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

/**
 * Because the builtin method is retarded and reverses it apparently
 */
fun Color.toIntBitsRGBA(): Int {
    return (255 * r).toInt() shl 24 or ((255 * g).toInt() shl 16) or ((255 * b).toInt() shl 8) or (255 * a).toInt()
}

