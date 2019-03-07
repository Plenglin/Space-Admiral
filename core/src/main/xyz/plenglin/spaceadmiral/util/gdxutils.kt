package xyz.plenglin.spaceadmiral.util

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

fun Camera.unproject2(vec: Vector3) = unproject(vec).let { Vector2(it.x, it.y) }
fun Camera.unproject2(vec: Vector2) = unproject(Vector3(vec, 0f)).let { Vector2(it.x, it.y) }
