package xyz.plenglin.spaceadmiral.net.game.io.c2s

import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.action.AttackSquadAction
import xyz.plenglin.spaceadmiral.game.action.MoveSquadAction
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.net.game.server.GamePlayerInterface
import java.util.*

data class ClearSquadActionQueueCommand(val recipient: UUID) : ClientCommand {
    override fun applyCommand(sender: GamePlayerInterface, instance: GameInstance): CommandResult {
        val recipient = instance.gameState.squads[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        if (sender.team.uuid != recipient.team.uuid) return CommandResult.SquadPermissions(sender, recipient)

        recipient.actionQueue.clear()
        recipient.stateScheduler.interrupt()
        return CommandResult.Success
    }
}

data class MoveSquadCommand(val recipient: UUID, val target: SquadTransform) : ClientCommand {
    override fun applyCommand(sender: GamePlayerInterface, instance: GameInstance): CommandResult {
        val recipient = instance.gameState.squads[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        if (sender.team.uuid != recipient.team.uuid) return CommandResult.SquadPermissions(sender, recipient)

        recipient.actionQueue.add(MoveSquadAction(recipient, target))
        return CommandResult.Success
    }
}

data class AttackSquadCommand(val recipient: UUID, val target: UUID) : ClientCommand {
    override fun applyCommand(sender: GamePlayerInterface, instance: GameInstance): CommandResult {
        val recipient = instance.gameState.squads[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        val target = instance.gameState.squads[target] ?: return CommandResult.InvalidData("No target $recipient!")
        if (sender.team.uuid != recipient.team.uuid) return CommandResult.SquadPermissions(sender, recipient)
        if (sender.team.uuid == target.team.uuid) return CommandResult.Fail("You can't attack your own team!")

        recipient.actionQueue.add(AttackSquadAction(recipient, target))
        return CommandResult.Success
    }
}
