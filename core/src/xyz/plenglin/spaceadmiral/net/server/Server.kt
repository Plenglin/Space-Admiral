package xyz.plenglin.spaceadmiral.net.server

import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.game.GameInstance

class Server(val players: List<PlayerInterface>) {
    fun update() {
        logger.trace("Updating server")
        instance.update()
    }

    val instance = GameInstance()

    companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(Server::class.java)
    }
}