package xyz.plenglin.spaceadmiral.net.io

import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.net.server.PlayerInterface
import java.io.Serializable

/**
 * A command that a client can send to mutate the game instance in some way.
 */
interface Command : Serializable {
    fun isPermitted(sender: PlayerInterface): Boolean
    fun applyCommand(instance: GameInstance)
}
