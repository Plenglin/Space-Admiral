package xyz.plenglin.spaceadmiral.util

import java.io.Serializable

class AdjustableClock : Serializable {
    private var lastTimeChange = 0L
    private var timeElapsed = 0.0
    var timeMultiplier = 1.0
        set(value) {
            timeElapsed = time
            lastTimeChange = System.currentTimeMillis()
            field = value
        }

    val time: Double get() = (System.currentTimeMillis() - lastTimeChange) * timeMultiplier / 1000 + timeElapsed
}
