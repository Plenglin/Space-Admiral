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
        return CommandResult.Success
    }

}

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
