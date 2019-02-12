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

        val tree = KDTree2<String>()
        val a = tree.insert(Vector2(0f, -1f), "A")
        tree.insert(Vector2(1f, 0f), "B")
        tree.insert(Vector2(-3f, 2f), "C")
        tree.insert(Vector2(-3f, 3f), "D")
        tree.insert(Vector2(2f, -4f), "E")
        tree.insert(Vector2(0.01f, -2f), "F")
        tree.insert(Vector2(0.8f, -1f), "G")
        tree.insert(Vector2(0.3f, -7f), "H")
        println(tree.root)
        println(a.successor())
        println(tree.root.findNearest(Vector2(1f, 1f)))
        a.remove()
        println(tree.root)
        System.exit(0)

        val config = LwjglApplicationConfiguration()
        LwjglApplication(SpaceAdmiral(), config)
    }
}
