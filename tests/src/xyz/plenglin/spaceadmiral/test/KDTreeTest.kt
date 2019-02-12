package xyz.plenglin.spaceadmiral.test

import org.junit.Test
import xyz.plenglin.spaceadmiral.util.KDTree2

class KDTreeTest {

    @Test
    fun test() {
        val tree = KDTree2<String>()
        println(tree.root)
    }

}