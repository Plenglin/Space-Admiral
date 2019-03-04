package xyz.plenglin.spaceadmiral.test

import com.badlogic.gdx.math.Vector2
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.plenglin.spaceadmiral.util.CircularTransformStructureException
import xyz.plenglin.spaceadmiral.util.Transform2D

class Transform2DTest {

    @Test
    fun testGlobalWithoutParent() {
        val trs = Transform2D(posLocal = Vector2(1f, 0f), angleLocal = 1f, parent = null)
        assertEquals(Vector2(1f, 0f), trs.posGlobal)
        assertEquals(1f, trs.angleGlobal)
    }

    @Test
    fun testGlobalWithParent() {
        val a = Transform2D(posLocal = Vector2(1f, 0f), angleLocal = (Math.PI / 2).toFloat(), parent = null)
        val b = Transform2D(posLocal = Vector2(1f, 0f), angleLocal = 2f, parent = a)
        assertEquals(1f, b.posGlobal.x, EPSILON)
        assertEquals(1f, b.posGlobal.y, EPSILON)
        assertEquals((Math.PI / 2).toFloat() + 2f, b.angleGlobal, EPSILON)
    }

    @Test
    fun testGlobalChangeParent() {
        val a = Transform2D(posLocal = Vector2(1f, 0f), angleLocal = (Math.PI / 2).toFloat(), parent = null)
        val b = Transform2D(posLocal = Vector2(1f, 0f), angleLocal = 2f, parent = null)
        assertEquals(Vector2(1f, 0f), b.posGlobal)
        assertEquals(2f, b.angleGlobal)

        b.parent = a

        assertEquals(1f, b.posGlobal.x, EPSILON)
        assertEquals(1f, b.posGlobal.y, EPSILON)
        assertEquals((Math.PI / 2).toFloat() + 2f, b.angleGlobal, EPSILON)
    }

    @Test(expected = CircularTransformStructureException::class)
    fun shouldFailOnCircularStructure() {
        val a = Transform2D(posLocal = Vector2(1f, 0f), angleLocal = (Math.PI / 2).toFloat(), parent = null)
        val b = Transform2D(posLocal = Vector2(1f, 0f), angleLocal = 2f, parent = null)
        b.parent = a
        a.parent = b
    }

    @Test(expected = CircularTransformStructureException::class)
    fun shouldFailOnSelfParent() {
        val a = Transform2D(posLocal = Vector2(1f, 0f), angleLocal = (Math.PI / 2).toFloat(), parent = null)
        a.parent = a
    }

    companion object {
        const val EPSILON = 1e-6f
    }

}