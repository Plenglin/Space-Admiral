package xyz.plenglin.spaceadmiral.util

interface State {
    /**
     * Called when the state is first loaded.
     */
    fun initialize(parent: StateScheduler)

    /**
     * Called every loop.
     */
    fun update()

    /**
     * @return should we terminate this state?
     */
    fun shouldTerminate(): Boolean

    /**
     * This state was overriden with another state
     */
    fun interrupt()

    /**
     * Naturally terminate this state.
     *
     * @return the next state to set
     */
    fun terminate(): State?
}

class StateScheduler {
    /**
     * The state that is running right now.
     */
    var currentState: State? = null
        private set

    /**
     * The state that will run when [currentState] finishes.
     */
    var defaultState: State? = null
        set(value) {
            field = value
            if (currentState == null) {
                nextState = value
            }
        }

    /**
     * Replace [currentState] with [nextState] next time [update] is called.
     */
    var nextState: State? = null

    private var wasInitialized = false
    private var shouldChangeState = false

    fun interrupt() {
        shouldChangeState = true
    }

    /**
     * Update this state machine.
     * @return true if a state ended this loop
     */
    fun update(): Boolean {
        // We have a new state
        val next = nextState
        if (next != null || shouldChangeState) {
            currentState?.interrupt()
            currentState = next
            nextState = null
            wasInitialized = false
        }

        // Update the current state
        var terminated = false
        currentState?.let {
            if (!wasInitialized) {
                it.initialize(this)
                wasInitialized = true
            }
            it.update()
            if (it.shouldTerminate()) {
                terminated = true
                nextState = it.terminate() ?: defaultState
            }
        }
        if (terminated) {
            currentState = null
        }
        return false
    }

}
