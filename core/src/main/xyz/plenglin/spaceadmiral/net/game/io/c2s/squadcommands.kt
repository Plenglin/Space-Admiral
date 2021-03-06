package xyz.plenglin.spaceadmiral.net.game.io.c2s

import xyz.plenglin.spaceadmiral.SectorID
import xyz.plenglin.spaceadmiral.SquadID
import xyz.plenglin.spaceadmiral.game.GameState
import xyz.plenglin.spaceadmiral.game.action.AttackSquadAction
import xyz.plenglin.spaceadmiral.game.action.MoveSquadAction
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.net.game.server.GamePlayerInterface

data class ClearSquadActionQueueCommand(val recipient: SquadID) : ClientCommand {
    override fun applyCommand(sender: GamePlayerInterface, gameState: GameState): CommandResult {
        val recipient = gameState[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        if (sender.team.uuid != recipient.team.uuid) return CommandResult.SquadPermissions(sender, recipient)

        recipient.actionQueue.clear()
        recipient.stateScheduler.interrupt()
        return CommandResult.Success
    }
}

data class MoveSquadCommand(val recipient: SquadID, val target: SquadTransform) : ClientCommand {
    override fun applyCommand(sender: GamePlayerInterface, gameState: GameState): CommandResult {
        val recipient = gameState[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        if (sender.team.uuid != recipient.team.uuid) return CommandResult.SquadPermissions(sender, recipient)

        recipient.actionQueue.add(MoveSquadAction(recipient, target))
        return CommandResult.Success
    }
}

data class AttackSquadCommand(val recipient: SquadID, val target: SquadID) : ClientCommand {
    override fun applyCommand(sender: GamePlayerInterface, gameState: GameState): CommandResult {
        val recipient = gameState[recipient] ?: return CommandResult.InvalidData("No recipient $recipient!")
        val target = gameState[target] ?: return CommandResult.InvalidData("No target $recipient!")
        if (sender.team.uuid != recipient.team.uuid) return CommandResult.SquadPermissions(sender, recipient)
        if (sender.team.uuid == target.team.uuid) return CommandResult.Fail("You can't attack your own team!")

        recipient.actionQueue.add(AttackSquadAction(recipient, target))
        return CommandResult.Success
    }
}

data class WarpSquadCommand(val recipients: List<SquadID>, val target: SectorID) : ClientCommand {
    override fun applyCommand(sender: GamePlayerInterface, gameState: GameState): CommandResult {
        val squadId = recipients.firstOrNull() ?: return CommandResult.InvalidData("No recipients supplied!")
        val squad = gameState[squadId] ?: return CommandResult.InvalidData("SquadID $squadId does not exist!")

        val dest = gameState[target]
        val bubble = squad.sector?.createWarpBubble(recipients.toSet(), dest)
        return CommandResult.Success
    }
}