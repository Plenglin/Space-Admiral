package xyz.plenglin.spaceadmiral.test

import com.badlogic.gdx.math.Vector2
import org.junit.Assert.*
import org.junit.Test
import xyz.plenglin.spaceadmiral.util.KDTree2
import xyz.plenglin.spaceadmiral.util.KDTree2Node
import java.lang.AssertionError
import java.lang.Exception
import java.util.*

class KDTreeTest {

    @Test
    fun testInsertion() {
        val tree = KDTree2<String>()
        val a = tree.insert(Vector2(0f, -1f), "A")
        tree.insert(Vector2(1f, 0f), "B")
        tree.insert(Vector2(-3f, 2f), "C")
        tree.insert(Vector2(-3f, 3f), "D")
        tree.insert(Vector2(2f, -4f), "E")

        tree.root.assertInvariant()
        assertEquals(true, tree.root.dimension)
        assertEquals(false, a.dimension)
        assertEquals(tree.root, a.parent)
        assertEquals(a, tree.root.c1)
    }

    @Test
    fun testSuccessor() {
        val tree = KDTree2<String>()
        tree.insert(Vector2(1f, 0f), "B")
        tree.insert(Vector2(-3f, 2f), "C")
        tree.insert(Vector2(-3f, 3f), "D")
        tree.insert(Vector2(2f, -4f), "E")
        val f = tree.insert(Vector2(0.01f, -2f), "F")
        tree.insert(Vector2(0.8f, -1f), "G")
        tree.insert(Vector2(0.3f, -5f), "H")
        assertEquals(f, tree.root.successor())
        tree.root.assertInvariant()

        val i = tree.insert(Vector2(-0.001f, -2f), "I")
        assertEquals(i, tree.root.successor())
        tree.root.assertInvariant()
    }

    @Test
    fun testRemoval() {
        val tree = KDTree2<String>()
        tree.insert(Vector2(1f, 0f), "B")
        tree.insert(Vector2(-3f, 2f), "C")
        tree.insert(Vector2(-3f, 3f), "D")
        tree.insert(Vector2(2f, -4f), "E")
        val f = tree.insert(Vector2(0.01f, -2f), "F")
        tree.insert(Vector2(0.8f, -1f), "G")
        tree.insert(Vector2(0.3f, -5f), "H")
        tree.insert(Vector2(-0.001f, -2f), "I")
        f.remove()

        tree.root.assertInvariant()
    }

    @Test
    fun emptyTreeShouldHaveNoNearest() {
        val random = Random(321)
        (1..100).forEach { _ ->
            val pos = Vector2(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1)
            val tree = KDTree2<Int>()
            assertEquals(null, tree.findNearest(pos).first)
        }
    }

    @Test
    fun testFindWithinSquare() {
        val random = Random(321)
        (1..100).forEach { _ ->
            val (tree, nodes) = createRandomTree(random, random.nextInt(30) + 10)
            val pos = Vector2(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1)
            val rad = random.nextFloat() / 2
            val inSquare = nodes.filter {
                Math.abs(it.key.x - pos.x) <= rad && Math.abs(it.key.y - pos.y) <= rad
            }.toMutableSet()
            val inSquareOriginal = inSquare.toSet()
            tree.findInSquare(pos, rad).forEach {
                try {
                    assertTrue(inSquare.remove(it))
                } catch (e: AssertionError) {
                    println(pos)
                    println(rad)
                    println(tree.root.toTreeJson())
                    println(it.key)
                    throw e
                }
            }
            try {
                assertTrue(inSquare.isEmpty())
            } catch (e: AssertionError) {
                println(pos)
                println(rad)
                println(tree.root.toTreeJson())
                println(inSquare.map { it.key })
                println(inSquareOriginal.map { it.key })
                throw e
            }
        }
    }

    @Test
    fun testFindWithinCircle() {
        val random = Random(321)
        (1..100).forEach { _ ->
            val (tree, nodes) = createRandomTree(random, random.nextInt(30) + 10)
            val pos = Vector2(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1)
            val rad = random.nextFloat() / 2
            val inSquare = nodes.filter {
                pos.dst(it.key) <= rad
            }.toMutableSet()
            val inSquareOriginal = inSquare.toSet()
            tree.findInCircle(pos, rad).forEach {
                try {
                    assertTrue(inSquare.remove(it))
                } catch (e: AssertionError) {
                    println(pos)
                    println(rad)
                    println(tree.root.toTreeJson())
                    println(it.key)
                    throw e
                }
            }
            try {
                assertTrue(inSquare.isEmpty())
            } catch (e: AssertionError) {
                println(pos)
                println(rad)
                println(tree.root.toTreeJson())
                println(inSquare.map { it.key })
                println(inSquareOriginal.map { it.key })
                throw e
            }
        }
    }

    @Test
    fun testNearest() {
        val random = Random(312124)

        (1..500).forEach { _ ->
            val (tree, nodes) = createRandomTree(random, random.nextInt(900) + 100)
            val pos = Vector2(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1)
            val trueNearest = nodes.minBy { it.key.dst2(pos) }!!
            try {
                assertEquals(trueNearest, tree.findNearest(pos).first)
            } catch (e: AssertionError) {
                println(pos)
                println(tree.root.toTreeJson())
                throw e
            }
        }
    }

    @Test
    fun testIteration() {
        val random = Random(213278)

        (1..500).forEach { _ ->
            val (tree, nodes) = createRandomTree(random, random.nextInt(900) + 100)
            val nodeSet = nodes.toMutableSet()
            val nodeList = tree.toList()
            for (n in nodeList) {
                try {
                    assertTrue(nodeSet.remove(n))
                } catch (e: AssertionError) {
                    println(n)
                    println(tree.root.toTreeJson())
                    println(nodeList)
                    println(nodeSet)
                    throw e
                }
            }
            assertTrue(nodeSet.isEmpty())
        }
    }

    @Test
    fun randomComprehensiveTest() {
        val random = Random(192913)
        (1..50).forEach { _ ->
            val (tree, nodes) = createRandomTree(random, random.nextInt(900) + 100)
            tree.root.assertInvariant()

            nodes.shuffle(random)  // Shuffle nodes for removal
            (1..10).forEach { _ ->
                when (random.nextInt(2)) {
                    0 -> {  // Remove a random node
                        val node = nodes.removeAt(nodes.size - 1)
                        node.remove()
                    }
                    1 -> {  // Insert an element
                        val node = tree.insert(Vector2(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1), random.nextInt(10000) + 10000)
                        nodes.add(node)
                    }
                }
                tree.root.assertInvariant()
            }
        }
    }

}

fun createRandomTree(random: Random, size: Int): Pair<KDTree2<Int>, ArrayList<KDTree2Node<Int>>> {
    val tree = KDTree2<Int>()
    val nodes = ArrayList<KDTree2Node<Int>>(size)
    (1..size).forEach {
        val node = tree.insert(Vector2(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1), it)
        nodes.add(node)
    }
    return tree to nodes
}

fun <T> KDTree2Node<T>.assertInvariant() {
    c0?.let {
        if (dimension) {
            assertTrue("$this first child must have x < it", it.key.x < this.key.x)
        } else {
            assertTrue("$this first child must have y < it", it.key.y < this.key.y)
        }
        assertNotEquals(dimension, it.dimension)
        it.assertInvariant()
    }
    c1?.let {
        if (dimension) {
            assertTrue("$this first child must have x >= it", it.key.x >= this.key.x)
        } else {
            assertTrue("$this first child must have y >= it", it.key.y >= this.key.y)
        }
        assertNotEquals(dimension, it.dimension)
        it.assertInvariant()
    }
}