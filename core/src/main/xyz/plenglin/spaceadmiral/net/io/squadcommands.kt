package xyz.plenglin.spaceadmiral.net.io

import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.squad.MoveSquadAction
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.net.server.PlayerInterface
import java.util.*

data class ClearSquadActionQueueCommand(val recipient: UUID) : ClientCommand {
    override fun applyCommand(sender: PlayerInterface, instance: GameInstance): CommandResult {
        val recipient = instance.gameState.squads[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        if (sender.team.uuid != recipient.team.uuid) return CommandResult.SquadPermissions(sender, recipient)

        recipient.actionQueue.clear()
        recipient.stateScheduler.interrupt()
        return CommandResult.Success
    }
}

/*
abstract class SquadActionCommand(val recipient: UUID) : ClientCommand {
    var shouldClearActionQueue: Boolean = true

    abstract fun createAction(sender: PlayerInterface, instance: GameInstance, recipient: Squad): SquadAction

    open fun checkPermissions(recipient: Squad): CommandResult {
        val recipient = instance.gameState.squads[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        if (sender.team.uuid != recipient.team.uuid) return CommandResult.SquadPermissions(sender, recipient)

        return CommandResult.Success
    }

    override fun applyCommand(sender: PlayerInterface, instance: GameInstance): CommandResult {
        val recipient = instance.gameState.squads[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        val result = checkPermissions(recipient)
        if (result == CommandResult.Success) {
            val action = createAction(sender, instance, recipient)
            if (shouldClearActionQueue) {
                recipient.actionQueue.clear()
                recipient.stateScheduler.interrupt()
            }
            recipient.actionQueue.add(action)
        }
        return result
    }
}
*/

data class MoveSquadCommand(val recipient: UUID, val target: SquadTransform) : ClientCommand {
    override fun applyCommand(sender: PlayerInterface, instance: GameInstance): CommandResult {
        val recipient = instance.gameState.squads[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        if (sender.team.uuid != recipient.team.uuid) return CommandResult.SquadPermissions(sender, recipient)

        recipient.actionQueue.add(MoveSquadAction(recipient, target))
        return CommandResult.Success
    }
}

data class AttackSquadCommand(val recipient: UUID, val target: UUID) : ClientCommand {
    override fun applyCommand(sender: PlayerInterface, instance: GameInstance): CommandResult {
        val recipient = instance.gameState.squads[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        val target = instance.gameState.squads[target] ?: return CommandResult.InvalidData("No target $recipient!")
        if (sender.team.uuid != recipient.team.uuid) return CommandResult.SquadPermissions(sender, recipient)
        if (sender.team.uuid == target.team.uuid) return CommandResult.Fail("You can't attack your own team!")

        // TODO recipient.actionQueue.add(AttackSquadAction(recipient, target))
        return CommandResult.Success
    }
}
