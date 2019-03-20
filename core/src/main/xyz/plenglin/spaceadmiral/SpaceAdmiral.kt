package xyz.plenglin.spaceadmiral

import com.badlogic.gdx.Game
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.ship.DummyFighter
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.net.game.local.GameLocalBridge
import xyz.plenglin.spaceadmiral.net.game.server.GameServer
import xyz.plenglin.spaceadmiral.view.screen.GameScreen
import java.util.*
import kotlin.concurrent.thread

class SpaceAdmiral : Game() {

    override fun create() {
        logger.info("Creating")
        val localBridge = GameLocalBridge(UUID.randomUUID())

        val server = GameServer(listOf(localBridge))
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

        val squad1 = server.instance.gameState.teams.values.first().createSquad(DummyFighter())
        squad1.transform.transform.angleLocal = 0.1f
        squad1.transform.transform.setLocalPosition(0f, 0f)
        squad1.resetShipPositions()

        val squad2 = server.instance.gameState.teams.values.first().createSquad(DummyFighter())
        squad2.transform.transform.angleLocal = 0.1f
        squad2.transform.transform.setLocalPosition(10f, 0f)
        squad2.resetShipPositions()

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

