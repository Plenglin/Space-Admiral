package xyz.plenglin.spaceadmiral.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import xyz.plenglin.spaceadmiral.SpaceAdmiral

object DesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        LwjglApplication(SpaceAdmiral(), LwjglApplicationConfiguration().apply {
            width = 1280
            height = 720
            resizable = false
            // vSyncEnabled = false
            // foregroundFPS = 0
            // backgroundFPS = 0
        })
    }
}
