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
                val start = System.currentTimeMillis()
                server.update()
                val elapsed = System.currentTimeMillis() - start
                if (elapsed < UPDATE_PERIOD) {
                    Thread.sleep(UPDATE_PERIOD - elapsed)
                }
            }
        }
        val squad = server.instance.gameState.team.values.first().createSquad(DummyFighter())
        squad.transform.transform.angleLocal = 0.1f
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

        const val UPDATE_PERIOD = 20L
    }
}

