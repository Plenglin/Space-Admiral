package xyz.plenglin.spaceadmiral

import com.badlogic.gdx.Game
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.net.client.Client
import xyz.plenglin.spaceadmiral.net.server.Server
import xyz.plenglin.spaceadmiral.net.server.generateEntangledPair
import xyz.plenglin.spaceadmiral.view.screen.GameScreen
import kotlin.concurrent.thread

class SpaceAdmiral : Game() {

    override fun create() {
        logger.info("Creating")
        val (pi, si) = generateEntangledPair()
        val server = Server(listOf(pi))
        val screen = GameScreen(Client(si))
        thread(start = true, isDaemon = true) {
            while (true) {
                server.update()
                Thread.sleep(20L)
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
    }
}

