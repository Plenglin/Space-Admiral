package xyz.plenglin.spaceadmiral.test

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertSame
import org.junit.Test
import xyz.plenglin.spaceadmiral.util.State
import xyz.plenglin.spaceadmiral.util.StateScheduler

class StateSchedulerTest {

    @Test
    fun testBasic() {
        val scheduler = StateScheduler()
        val a = TestState()

        assertEquals(TestState(0, 0, 0, 0,0), a)
        scheduler.nextState = a
        assertSame(a, scheduler.nextState)
        assertEquals(TestState(0, 0, 0, 0,0), a)

        scheduler.update()
        assertSame(a, scheduler.currentState)
        assertEquals(null, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 0,0), a)

        scheduler.update()
        assertSame(a, scheduler.currentState)
        assertEquals(TestState(1, 2, 2, 0,0), a)

        a.shouldTerminate = true
        scheduler.update()
        assertEquals(null, scheduler.nextState)
        assertEquals(null, scheduler.currentState)
        assertEquals(TestState(1, 3, 3, 0,1), a)
    }

    @Test
    fun testSimpleInterrupt() {
        val scheduler = StateScheduler()
        val a = TestState()

        scheduler.nextState = a

        scheduler.update()
        assertSame(a, scheduler.currentState)
        assertEquals(null, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 0,0), a)

        scheduler.interrupt()
        assertSame(a, scheduler.currentState)
        assertEquals(null, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 0,0), a)

        scheduler.update()
        assertEquals(null, scheduler.currentState)
        assertEquals(null, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 1,0), a)
    }

    @Test
    fun shouldInterruptOnReplace() {
        val scheduler = StateScheduler()
        val a = TestState()
        val b = TestState()

        scheduler.nextState = a

        scheduler.update()
        assertSame(a, scheduler.currentState)
        assertEquals(null, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 0,0), a)
        assertEquals(TestState(0, 0, 0, 0,0), b)

        scheduler.nextState = b
        assertSame(a, scheduler.currentState)
        assertSame(b, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 0,0), a)
        assertEquals(TestState(0, 0, 0, 0,0), b)

        scheduler.update()
        assertSame(b, scheduler.currentState)
        assertSame(null, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 1,0), a)
        assertEquals(TestState(1, 1, 1, 0,0), b)
    }

    @Test
    fun testInterruptThenReplace() {
        val scheduler = StateScheduler()
        val a = TestState()
        val b = TestState()

        scheduler.nextState = a

        scheduler.update()
        assertSame(a, scheduler.currentState)
        assertEquals(null, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 0,0), a)
        assertEquals(TestState(0, 0, 0, 0,0), b)

        scheduler.interrupt()
        scheduler.nextState = b
        assertSame(a, scheduler.currentState)
        assertSame(b, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 0,0), a)
        assertEquals(TestState(0, 0, 0, 0,0), b)

        scheduler.update()
        assertSame(b, scheduler.currentState)
        assertSame(null, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 1,0), a)
        assertEquals(TestState(1, 1, 1, 0,0), b)

        scheduler.update()
        assertSame(b, scheduler.currentState)
        assertSame(null, scheduler.nextState)
        assertEquals(TestState(1, 1, 1, 1,0), a)
        assertEquals(TestState(1, 2, 2, 0,0), b)
    }

}

data class TestState(
        var nInit: Int = 0,
        var nUpdate: Int = 0,
        var nShouldTerminate: Int = 0,
        var nInterrupt: Int = 0,
        var nTerminate: Int = 0) : State {


    var shouldTerminate: Boolean = false
    var nextState: State? = null

    override fun initialize(parent: StateScheduler) {
        nInit++
    }

    override fun update() {
        nUpdate++
    }

    override fun shouldTerminate(): Boolean {
        nShouldTerminate++
        return shouldTerminate
    }

    override fun interrupt() {
        nInterrupt++
    }

    override fun terminate(): State? {
        nTerminate++
        return nextState
    }

}