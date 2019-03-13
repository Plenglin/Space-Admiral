package xyz.plenglin.spaceadmiral.net.io

import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.squad.Squad
import xyz.plenglin.spaceadmiral.net.server.PlayerInterface
import java.io.Serializable

sealed class CommandResult {
    object Success : CommandResult()
    open class Fail(val reason: String? = null) : CommandResult() {
        override fun toString(): String = "Fail($reason)"
    }
    class InvalidData(reason: String) : Fail(reason) {
        override fun toString(): String = "InvalidData($reason)"
    }
    data class SquadPermissions(val sender: PlayerInterface, val recipient: Squad) : Fail("User $sender is not allowed to send commands to $recipient!")
}


/**
 * A command that a client can send to mutate the game instance in some way.
 */
interface ClientCommand : Serializable {
    /**
     * Called on the server side. Attempt to apply this command to the [GameInstance]. In the event of a failure, there
     * should be NO changes applied.
     *
     * @param sender the player who sent it
     * @param instance the [GameInstance] to apply it on
     * @return the result
     */
    fun applyCommand(sender: PlayerInterface, instance: GameInstance): CommandResult
}
