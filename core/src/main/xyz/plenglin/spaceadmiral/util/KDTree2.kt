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

    /**
     * Find the nearest node to the given position that satisfies the given predicate.
     *
     * Implemented using a DFS
     */
    fun findNearest(pos: Vector2, filter: (KDTree2Node<T>) -> Boolean = { true }): Pair<KDTree2Node<T>?, Float> {
        val stack = LinkedList<KDTree2Node<T>>()
        root.c0?.let(stack::push)
        root.c1?.let(stack::push)
        var nearestN: KDTree2Node<T>? = null
        var nearestR = Float.POSITIVE_INFINITY

        // Perform a modified DFS through the tree
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
                    if (pos.x + nearestR >= node.key.x) {  // Does our circle touch the right side?
                        node.c1?.let(stack::push)  // Let in the right child
                    }
                } else {  // Right side
                    node.c1?.let(stack::push)  // Add right child
                    if (pos.x - nearestR < node.key.x) {  // Does our circle reach the left side?
                        node.c0?.let(stack::push)  // Let in the left child
                    }
                }
            } else {
                if (pos.y < node.key.y) {  // Bottom side
                    node.c0?.let(stack::push)  // Add bottom child
                    if (pos.y + nearestR >= node.key.y) {  // Does our circle reach the top side?
                        node.c1?.let(stack::push)  // Let in the top child
                    }
                } else {  // Top side
                    node.c1?.let(stack::push)  // Add top child
                    if (pos.y - nearestR < node.key.y) {  // Does our circle reach the bottom side?
                        node.c0?.let(stack::push)  // Let in the bottom child
                    }
                }
            }
        }
        return nearestN to nearestR
    }

    fun findInRect(x0: Float, x1: Float, y0: Float, y1: Float): Sequence<KDTree2Node<T>> = sequence {
        val stack = LinkedList<KDTree2Node<T>>()
        root.c0?.let(stack::push)
        root.c1?.let(stack::push)

        // Perform a modified DFS through the tree
        while (stack.isNotEmpty()) {
            val node = stack.pop()

            // Add nodes
            if (node.key.x in x0..x1 && node.key.y in y0..y1) {  // Is the current node in the region?
                yield(node)
            }

            // Filter out regions
            if (node.dimension) {  // x-dimension
                if (x0 <= node.key.x) {
                    node.c0?.let(stack::push)  // Add right child
                }
                if (node.key.x <= x1) {
                    node.c1?.let(stack::push)  // Add left child
                }
            } else {
                if (y0 <= node.key.y) {
                    node.c0?.let(stack::push)  // Add top child
                }
                if (node.key.y <= y1) {
                    node.c1?.let(stack::push)  // Add bottom child
                }
            }
        }
    }

    fun findInCircle(pos: Vector2, radius: Float): Sequence<KDTree2Node<T>> {
        val r2 = radius * radius
        return findInSquare(pos, radius)
                .filter { it.key.dst2(pos) <= r2 }
    }

    /**
     * Find all nodes within a square radius of the given position.
     */
    fun findInSquare(pos: Vector2, radius: Float): Sequence<KDTree2Node<T>> = findInRect(
        x0 = pos.x - radius,
        x1 = pos.x + radius,
        y0 = pos.y - radius,
        y1 = pos.y + radius
    )

    fun clear() {
        root.c0 = null
        root.c1 = null
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
        val parent = find(node.key)
        parent.let {
            if (it.dimension) {
                if (node.key.x < it.key.x) {
                    it.attachC0(node)
                } else {
                    it.attachC1(node)
                }
            } else {
                if (node.key.y < it.key.y) {
                    it.attachC0(node)
                } else {
                    it.attachC1(node)
                }
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

    private fun nextChild(pos: Vector2): KDTree2Node<T>? {
        return if (dimension) {
            if (pos.x < key.x) {
                c0
            } else {
                c1
            }
        } else {
            if (pos.y < key.y) {
                c0
            } else {
                c1
            }
        }
    }

    /**
     * Find the smallest region that holds pos.
     */
    fun find(pos: Vector2): KDTree2Node<T> {
        var par = this
        var node: KDTree2Node<T>? = this
        while (node != null) {
            par = node
            node = node.nextChild(pos)
        }
        return par
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KDTree2Node<*>

        if (key != other.key) return false
        if (value != other.value) return false

        return true
    }

}

