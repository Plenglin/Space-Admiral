package xyz.plenglin.spaceadmiral.util

import com.badlogic.gdx.math.Vector2

class KDTree2<T> : Iterable<KDTree2Node<T>> {
    val root = KDTree2Node<T>(Vector2(0f, 0f), null, null, true)

    override fun iterator(): Iterator<KDTree2Node<T>> {
        return root.iterator()
    }

}

data class KDTree2Node<T>(
        var key: Vector2,
        var value: T?,
        var parent: KDTree2Node<T>?,
        /**
         * true => compare in x; false => compare in y
         */
        var dimension: Boolean,
        var c0: KDTree2Node<T>? = null,
        var c1: KDTree2Node<T>? = null)
    : Iterable<KDTree2Node<T>> {

    fun insert(node: KDTree2Node<T>) {
        c0?.let {
            it.insert(node)
        } ?: c1?.let {
            it.insert(node)
        } ?: insertDirectly(node)
    }

    private fun insertDirectly(node: KDTree2Node<T>) {
        if (dimension) {
            if (node.key.x < key.x) {
                c0 = node
            } else {
                c1 = node
            }
        } else {
            if (node.key.y < key.y) {
                c0 = node
            } else {
                c1 = node
            }
        }
    }

    override fun iterator(): Iterator<KDTree2Node<T>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

