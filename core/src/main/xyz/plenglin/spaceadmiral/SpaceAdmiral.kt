package xyz.plenglin.spaceadmiral

import com.badlogic.gdx.Game
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.ship.DummyCorvette
import xyz.plenglin.spaceadmiral.game.ship.DummyFighter
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.net.game.local.GameDummyPlayer
import xyz.plenglin.spaceadmiral.net.game.local.GameLocalBridge
import xyz.plenglin.spaceadmiral.net.game.server.GameServer
import xyz.plenglin.spaceadmiral.view.screen.GameScreen
import java.util.*
import kotlin.concurrent.thread

class SpaceAdmiral : Game() {

    override fun create() {
        logger.info("Creating")
        val localBridge = GameLocalBridge(UUID.randomUUID())
        val dummy = GameDummyPlayer(UUID.randomUUID())

        val server = GameServer(listOf(localBridge, dummy))
        val screen = GameScreen(GameClient(localBridge))
        val teams = server.instance.gameState.teams
        val t1 = teams[localBridge.team]!!
        val t2 = teams[dummy.team]!!

        t1.createSquad(DummyFighter).apply {
            transform.transform.angleLocal = 0.1f
            transform.transform.setLocalPosition(0f, 0f)
            resetShipPositions()
        }

        t1.createSquad(DummyFighter).apply {
            transform.transform.setLocalPosition(10f, 0f)
            resetShipPositions()
        }

        t2.createSquad(DummyFighter).apply {
            transform.transform.setLocalPosition(10f, 20f)
            resetShipPositions()
        }

        t1.createSquad(DummyCorvette).apply {
            transform.transform.setLocalPosition(-30f, 20f)
            resetShipPositions()
        }

        t2.createSquad(DummyCorvette).apply {
            transform.transform.setLocalPosition(-20f, 20f)
            resetShipPositions()
        }

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

        setScreen(screen)
    }

    override fun dispose() {
        super.dispose()
        logger.info("Disposing")
    }

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(SpaceAdmiral::class.java)

        const val PORT = 42069
        const val UPDATE_PERIOD = 20L
    }
}

