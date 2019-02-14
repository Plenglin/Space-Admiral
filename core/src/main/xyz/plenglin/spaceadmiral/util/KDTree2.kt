package xyz.plenglin.spaceadmiral.util

import com.badlogic.gdx.math.Vector2
import java.util.*

class KDTree2<T> : Iterable<KDTree2Node<T>> {
    val root = KDTree2Node<T>(Vector2(0f, 0f), null, null, true)


    override fun iterator(): Iterator<KDTree2Node<T>> = object : Iterator<KDTree2Node<T>> {
        private val stack = LinkedList<KDTree2Node<T>>()

        init {
            root.c0?.let(stack::push)
            root.c1?.let(stack::push)
        }

        override fun hasNext(): Boolean {
            return stack.isNotEmpty()
        }

        override fun next(): KDTree2Node<T> {
            val node = stack.pop()
            node.c0?.let(stack::push)
            node.c1?.let(stack::push)
            return node
        }

    }

    fun insert(key: Vector2, value: T): KDTree2Node<T> {
        val node = KDTree2Node(key, value)
        root.insert(node)
        return node
    }

    fun findNearest(pos: Vector2, filter: (KDTree2Node<T>) -> Boolean = { true }): Pair<KDTree2Node<T>, Float>? {
        val out = root.findNearest(pos, filter)
        if (out.first == root) {
            return null
        }
        return out
    }
}

class KDTree2Node<T>(
        var key: Vector2,
        var value: T?,
        var parent: KDTree2Node<T>? = null,
        /**
         * true => compare in x; false => compare in y
         */
        var dimension: Boolean = true,

        /**
         * Anything in the range of (-inf, 0)
         */
        var c0: KDTree2Node<T>? = null,
        /**
         * Anything in the range of [0, inf)
         */
        var c1: KDTree2Node<T>? = null) {

    private fun attachC0(node: KDTree2Node<T>) {
        c0 = node
        node.parent = this
        node.dimension = !dimension
    }

    private fun attachC1(node: KDTree2Node<T>) {
        c1 = node
        node.parent = this
        node.dimension = !dimension
    }

    fun insert(node: KDTree2Node<T>) {
        if (dimension) {
            if (node.key.x < key.x) {
                c0?.insert(node) ?: attachC0(node)
            } else {
                c1?.insert(node) ?: attachC1(node)
            }
        } else {
            if (node.key.y < key.y) {
                c0?.insert(node) ?: attachC0(node)
            } else {
                c1?.insert(node) ?: attachC1(node)
            }
        }
    }

    fun remove() {
        val succ = successor()
        succ?.let {
            it.remove()
            it.c0 = c0
            it.c1 = c1
            it.dimension = dimension
        }
        c0?.parent = succ
        c1?.parent = succ
        parent?.let {
            if (it.c0 == this) {
                it.c0 = succ
            } else {
                it.c1 = succ
            }
        }
    }

    /**
     * Returns the node that is closest to this node in the dimension of comparison.
     * @return the successor, or null if this node is a leaf
     */
    fun successor(): KDTree2Node<T>? {
        /**
         * Implementation details: It performs a depth-first search on the tree. It eliminates
         * possibilities along the way.
         */
        val stack = LinkedList<KDTree2Node<T>>()
        var out = c0
        c0?.let(stack::push)
        c1?.let(stack::push)
        while (stack.isNotEmpty()) {
            val node = stack.pop()!!
            out = pickCloser(node, out)
            if (node.dimension != this.dimension) {
                node.c0?.let(stack::push)
                node.c1?.let(stack::push)
            } else {
                pickCloser(node.c0, node.c1)?.let {
                    stack.push(it)
                }
            }
        }
        return out
    }

    private fun pickCloser(n1: KDTree2Node<T>?, n2: KDTree2Node<T>?): KDTree2Node<T>? {
        return when {
            n1 == null -> n2
            n2 == null -> n1
            dimension -> if (Math.abs(n1.key.x - key.x) < Math.abs(n2.key.x - key.x)) {
                    n1
                } else {
                    n2
                }
            else -> if (Math.abs(n1.key.y - key.y) < Math.abs(n2.key.y - key.y)) {
                    n1
                } else {
                    n2
                }
        }
    }

    /**
     * Find the nearest node to the given position that satisfies the given predicate.
     *
     * Implemented using a DFS
     */
    fun findNearest(pos: Vector2, filter: (KDTree2Node<T>) -> Boolean = { true }): Pair<KDTree2Node<T>, Float> {
        val stack = LinkedList<KDTree2Node<T>>()
        stack.push(this)
        var nearestN = if (this.parent == null) {
            c0 ?: c1 ?: return this to Float.NaN
        } else this
        var nearestR = nearestN.key.dst(pos)

        // Perform a DFS through the tree
        while (stack.isNotEmpty()) {
            val node = stack.pop()
            val r = pos.dst(node.key)
            if (r < nearestR && filter(node) && node.parent != null) {  // Is the current node closer?
                nearestR = r
                nearestN = node
            }
            if (node.dimension) {  // x-dimension
                if (pos.x < node.key.x) {  // Left side
                    node.c0?.let(stack::push)  // Add left child
                    if (pos.x + nearestR >= node.key.x) {  // Does our circle touch the left side?
                        node.c1?.let(stack::push)
                    }
                } else {  // Right side
                    node.c1?.let(stack::push)  // Add right child
                    if (pos.x - nearestR < node.key.x) {  // Does our circle reach the left side?
                        node.c0?.let(stack::push)
                    }
                }
            } else {
                if (pos.y < node.key.y) {  // Bottom side
                    node.c0?.let(stack::push)  // Add bottom child
                    if (pos.y + nearestR >= node.key.y) {  // Does our circle reach the top side?
                        node.c1?.let(stack::push)
                    }
                } else {  // Top side
                    node.c1?.let(stack::push)  // Add top child
                    if (pos.y - nearestR < node.key.y) {  // Does our circle reach the bottom side?
                        node.c0?.let(stack::push)
                    }
                }
            }
        }
        return nearestN to nearestR
    }

    override fun toString(): String {
        return "KDTree2Node(${if (dimension) "X" else "Y"}: $key, $value, $c0, $c1)"
    }

    fun toTreeJson(): String {
        return """{"key":[${key.x}, ${key.y}],"value":"$value","dim":$dimension,"c0":${c0?.toTreeJson()},"c1":${c1?.toTreeJson()}}"""
    }

    override fun hashCode(): Int {
        return (key.hashCode() * 31) or value.hashCode()
    }

}

