package xyz.plenglin.spaceadmiral.util

import com.badlogic.gdx.math.Vector2

class LinearInterpolator2(private val inputPeriod: Float) {

    val x0 = Vector2()
    val x1 = Vector2()

    private var t: Float = 0f

    fun pushActual(x: Vector2) {
        t %= 1
        x0.set(x1)
        x1.set(x)
    }

    fun pushSample(dt: Float): Vector2 {
        t = (t + dt / inputPeriod).coerceIn(0f, 1f)
        return lerp(t, x0, x1)
    }

}