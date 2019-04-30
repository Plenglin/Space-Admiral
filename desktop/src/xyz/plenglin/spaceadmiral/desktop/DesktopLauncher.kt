package xyz.plenglin.spaceadmiral.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import xyz.plenglin.spaceadmiral.SpaceAdmiral

object DesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        /*val options = Options().apply {
            addOption("s", "server",  true, "start as a server, listening on provided port")
            addOption("h", "host",  true, "start a client and server, listening on provided port")
            addOption("c", "client", true, "start as a client, connecting to provided ip")
        }

        val cmd = DefaultParser().parse(options, args)*/

        val config = LwjglApplicationConfiguration().apply {
            width = 1280
            height = 720
            resizable = false
        }
        LwjglApplication(SpaceAdmiral(), config)
    }
}
