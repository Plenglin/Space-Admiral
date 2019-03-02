package xyz.plenglin.spaceadmiral

import com.badlogic.gdx.Game
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.ship.DummyFighter
import xyz.plenglin.spaceadmiral.net.client.GameClient
import xyz.plenglin.spaceadmiral.net.local.LocalBridge
import xyz.plenglin.spaceadmiral.net.server.Server
import xyz.plenglin.spaceadmiral.view.screen.GameScreen
import kotlin.concurrent.thread

class SpaceAdmiral : Game() {

    override fun create() {
        logger.info("Creating")
        val localBridge = LocalBridge()
        val server = Server(listOf(localBridge))
        val screen = GameScreen(GameClient(localBridge))
        thread(start = true, isDaemon = true) {
            while (true) {
                server.update()
                Thread.sleep(20L)
            }
        }
        val squad = server.instance.gameState.team.values.first().createSquad(DummyFighter())
        //squad.transform.angleLocal = 0.1f
        //squad.transform.update()
        squad.resetShipPositions()

        setScreen(screen)
    }

    override fun dispose() {
        super.dispose()
        logger.info("Disposing")
    }

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(SpaceAdmiral::class.java)
    }
}

