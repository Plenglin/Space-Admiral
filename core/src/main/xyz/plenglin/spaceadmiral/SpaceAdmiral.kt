package xyz.plenglin.spaceadmiral

import com.badlogic.gdx.Game
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.ship.DummyCorvette
import xyz.plenglin.spaceadmiral.game.ship.DummyFighter
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.net.game.io.s2c.initial.toInitialDTO
import xyz.plenglin.spaceadmiral.net.game.local.GameDummyPlayer
import xyz.plenglin.spaceadmiral.net.game.local.GameLocalBridge
import xyz.plenglin.spaceadmiral.net.game.server.GameServer
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.view.screen.GridScreen
import java.util.*
import kotlin.concurrent.thread

object SpaceAdmiral : Game() {

    @JvmStatic
    val logger = LoggerFactory.getLogger(SpaceAdmiral::class.java)

    const val PORT = 42069
    const val UPDATE_PERIOD = 20L
    const val GRID_SIZE = 21

    override fun create() {
        logger.info("Creating")
        val gameInstance = GameInstance()
        val payload = gameInstance.toInitialDTO()

        val localBridge = GameLocalBridge(UUID.randomUUID(), payload)
        val dummy = GameDummyPlayer(UUID.randomUUID())

        val server = GameServer(localBridge, dummy, instance = gameInstance)
        val screen = GridScreen(GameClient(localBridge))

        val instance = server.instance
        val teams = instance.gameState.teams
        val t1 = teams[localBridge.team]!!
        val t2 = teams[dummy.team]!!
        val sector = instance.gameState.getSector(IntVector2(0, 0))

        t1.createSquad(DummyFighter, sector).apply {
            transform.transform.angleLocal = 0.1f
            transform.transform.setLocalPosition(0f, 0f)
            resetShipPositions()
        }

        t1.createSquad(DummyFighter, sector).apply {
            transform.transform.setLocalPosition(10f, 0f)
            resetShipPositions()
        }

        t2.createSquad(DummyFighter, sector).apply {
            transform.transform.setLocalPosition(10f, 20f)
            resetShipPositions()
        }

        t1.createSquad(DummyCorvette, sector).apply {
            transform.transform.setLocalPosition(-30f, 20f)
            resetShipPositions()
        }

        t2.createSquad(DummyCorvette, sector).apply {
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
}

