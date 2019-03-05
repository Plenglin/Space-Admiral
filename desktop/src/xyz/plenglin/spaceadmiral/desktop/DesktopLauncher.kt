package xyz.plenglin.spaceadmiral.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.SpaceAdmiral
import xyz.plenglin.spaceadmiral.util.KDTree2
import xyz.plenglin.spaceadmiral.util.KDTree2Node

object DesktopLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        LwjglApplication(SpaceAdmiral(), LwjglApplicationConfiguration().apply {
            width = 1280
            height = 720
            // vSyncEnabled = false
            // foregroundFPS = 0
            // backgroundFPS = 0
        })
    }
}
