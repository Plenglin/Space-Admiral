package xyz.plenglin.spaceadmiral.game

import com.badlogic.gdx.math.Vector2
import java.util.*

class Transform2D(val posLocal: Vector2, var angleLocal: Float) {

    private var _dirty = true
    var dirty
        get() = _dirty
        set(value) {
            val queue = LinkedList<Transform2D>()
            queue.add(this)
            if (value) {
                children.forEach { it._dirty = true }
                queue.addAll(children)
            }
        }

    val posGlobal: Vector2 = Vector2()
    var angleGlobal: Float = angleLocal
        private set

    var parent: Transform2D? = null
        set(value) {
            field?.children?.remove(this)
            value?.children?.add(this)
            dirty = true
            field = value
        }

    private val children = mutableListOf<Transform2D>()

    private fun updateSelf() {
        if (dirty) {
            parent?.let {
                posGlobal.set(it.posGlobal).add(posLocal.cpy().rotateRad(it.angleGlobal))
                angleGlobal = it.angleGlobal + angleLocal
            } ?: kotlin.run {
                posGlobal.set(posLocal)
                angleGlobal = angleLocal
            }
            dirty = false
        }
    }

    private fun update() {
        var node = this
        val toUpdate = LinkedList<Transform2D>()
        while (node.parent != null) {
            toUpdate.push(node)
            node = node.parent!!
        }
        toUpdate.forEach(Transform2D::updateSelf)
    }

    companion object {
        @JvmStatic
        val ZERO = Transform2D(Vector2.Zero, 0f)
    }
}

fun Vector2.multCpx(o: Vector2): Vector2 {
    x = x * o.x - y * o.y
    y = x * o.y + y * o.x
    return this
}
