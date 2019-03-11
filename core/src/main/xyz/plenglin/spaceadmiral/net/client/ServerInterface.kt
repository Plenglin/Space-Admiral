package xyz.plenglin.spaceadmiral.net.client

import xyz.plenglin.spaceadmiral.game.squad.SquadAction
import java.util.*

/**
 * How clients see the server.
 */
interface ServerInterface {
    val connected: Boolean
    val clientTeam: UUID

    fun attachClient(client: GameClient)
    fun sendSquadAction(action: SquadAction)
    fun clearSquadActionQueue(squad: UUID)

    fun sendDisconnectToServer()
}
