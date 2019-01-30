package xyz.plenglin.spaceadmiral.util

import java.util.*

sealed class Trigger

data class SleepTrigger(val timeout: Int) : Trigger()
data class EventTrigger(val event: String, val timeout: Long = -1) : Trigger() {
    var data: Any? = null
}

data class Event(val name: String, val data: Any)

typealias Coroutine = Sequence<Trigger>

class Future(val coroutine: Coroutine) {
    val iterator = coroutine.iterator()
}

class EventLoop(val clock: AdjustableClock) {
    private val sleeping = PriorityQueue<SuspendedFuture>()
    private val eventing = mutableMapOf<String, LinkedList<SuspendedFuture>>()

    private val addBuffer = LinkedList<Coroutine>()
    private val eventBuffer = LinkedList<Event>()

    fun schedule(coroutine: Coroutine) {
        addBuffer.add(coroutine)
    }

    fun update() {
        addBuffer.forEach {
            processFuture(Future(it))
        }
        while (sleeping.peek().time < clock.time) {
            val suspended = sleeping.remove()
            if (!suspended.fulfilled) {
                processFuture(suspended.future)
                suspended.fulfilled = true
            }
        }
        eventBuffer.forEach { ev ->
            eventing[ev.name]?.forEach { suspended ->
                (suspended.trigger as EventTrigger).data = ev.data
                if (!suspended.fulfilled) {
                    processFuture(suspended.future)
                    suspended.fulfilled = true
                }
            }
        }
    }

    private fun processFuture(future: Future) {
        if (!future.iterator.hasNext()) {
            return
        }
        val trigger = future.iterator.next()
        when (trigger) {
            is SleepTrigger -> sleeping.add(SuspendedFuture(future, trigger, clock.time + trigger.timeout))
            is EventTrigger -> {
                val suspended = SuspendedFuture(future, trigger, clock.time + trigger.timeout)
                eventing[trigger.event]?.add(suspended)
                if (trigger.timeout > 0) {
                    sleeping.add(suspended)
                }
            }
        }
    }

    private data class SuspendedFuture(val future: Future, val trigger: Trigger, val time: Double, var fulfilled: Boolean = false) : Comparable<SuspendedFuture> {
        override fun compareTo(other: SuspendedFuture): Int = when {
            time < other.time -> -1
            time > other.time -> 1
            else -> 0
        }
    }

}
