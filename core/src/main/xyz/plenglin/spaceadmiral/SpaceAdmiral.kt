package xyz.plenglin.spaceadmiral

import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import ktx.scene2d.Scene2DSkin
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameInstance
import xyz.plenglin.spaceadmiral.game.squad.DummyCorvette
import xyz.plenglin.spaceadmiral.game.squad.DummyCruiser
import xyz.plenglin.spaceadmiral.game.squad.DummyFighter
import xyz.plenglin.spaceadmiral.net.game.client.GameClient
import xyz.plenglin.spaceadmiral.net.game.io.s2c.initial.toInitialDTO
import xyz.plenglin.spaceadmiral.net.game.local.GameDummyPlayer
import xyz.plenglin.spaceadmiral.net.game.local.GameLocalBridge
import xyz.plenglin.spaceadmiral.net.game.server.GameServer
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.view.screen.GridScreen
import kotlin.concurrent.thread

class SpaceAdmiral : Game() {

    lateinit var assets: AssetManager

    override fun create() {
        logger.info("Creating")

        logger.info("Loading assets")
        assets = AssetManager()
        assets.load(ASSET_SKIN)
        assets.load(ASSET_UI_ATLAS)
        assets.load(ASSET_GAMESPRITE_ATLAS)
        assets.finishLoading()
        logger.info("Assets finished loading")

        Scene2DSkin.defaultSkin = assets.get(ASSET_SKIN)

        val gameInstance = GameInstance()

        val localBridge = GameLocalBridge(nextTeamID())
        val dummy = GameDummyPlayer(nextTeamID())

        val server = GameServer(localBridge, dummy, instance = gameInstance)

        val instance = server.instance
        val gameState = instance.gameState
        val t1 = gameState[localBridge.team]!!
        val t2 = gameState[dummy.team]!!
        val sector = gameState[IntVector2(0, 0)]

        t1.createSquad(DummyFighter, sector).apply {
            transform.transform.angleLocal = 0.1f
            transform.transform.setLocalPosition(0f, 0f)
            resetShipPositions()
        }

        t1.createSquad(DummyFighter, sector).apply {
            transform.transform.setLocalPosition(10f, 0f)
            resetShipPositions()
        }

        t1.createSquad(DummyFighter, sector).apply {
            transform.transform.angleLocal = 0.1f
            transform.transform.setLocalPosition(-14f, 53f)
            resetShipPositions()
        }

        t1.createSquad(DummyFighter, sector).apply {
            transform.transform.setLocalPosition(36f, 23f)
            resetShipPositions()
        }

        t1.createSquad(DummyCorvette, sector).apply {
            transform.transform.setLocalPosition(-30f, 20f)
            resetShipPositions()
        }

        t1.createSquad(DummyCruiser, sector).apply {
            transform.transform.setLocalPosition(43f, -23f)
            resetShipPositions()
        }

        t2.createSquad(DummyFighter, sector).apply {
            transform.transform.setLocalPosition(10f, 20f)
            resetShipPositions()
        }

        t2.createSquad(DummyCorvette, sector).apply {
            transform.transform.setLocalPosition(-20f, 20f)
            resetShipPositions()
        }

        val initialPayload = gameInstance.toInitialDTO()
        localBridge.initialPayload = initialPayload
        localBridge.lockServerSide.unlock()
        logger.debug("{}", initialPayload)

        val localClient = GameClient(localBridge)

        thread(name = "Space Admiral Server", start = true, isDaemon = true) {
            while (true) {
                val start = System.currentTimeMillis()
                server.update()
                val elapsed = System.currentTimeMillis() - start
                if (elapsed < UPDATE_PERIOD) {
                    Thread.sleep(UPDATE_PERIOD - elapsed)
                }
            }
        }

        val screen = GridScreen(this, assets, localClient)
        setScreen(screen)
    }

    override fun dispose() {
        super.dispose()
        logger.info("Disposing")
        assets.dispose()
    }

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(SpaceAdmiral::class.java)

        const val PORT = 42069
        const val UPDATE_PERIOD = 50L
        const val DELTA_TIME = UPDATE_PERIOD / 1000f
        const val GRID_SIZE = 21
    }

}

