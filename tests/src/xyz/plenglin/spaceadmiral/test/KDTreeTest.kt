package xyz.plenglin.spaceadmiral.test

import com.badlogic.gdx.math.Vector2
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import xyz.plenglin.spaceadmiral.util.KDTree2
import xyz.plenglin.spaceadmiral.util.KDTree2Node
import java.util.*

class KDTreeTest {

    @Test
    fun testInsertion() {
        val tree = KDTree2<String>()
        val a = tree.insert(Vector2(0f, -1f), "A")
        tree.insert(Vector2(1f, 0f), "B")
        tree.insert(Vector2(-3f, 2f), "C")
        tree.insert(Vector2(-3f, 3f), "D")
        tree.insert(Vector2(2f, -4f), "E")

        tree.root.assertInvariant()
        assertEquals(true, tree.root.dimension)
        assertEquals(false, a.dimension)
        assertEquals(tree.root, a.parent)
        assertEquals(a, tree.root.c1)
        assertEquals("((_ <- C(-3.0,2.0) -> (_ <- D(-3.0,3.0) -> _)) <- null(0.0,0.0) -> ((_ <- E(2.0,-4.0) -> _) <- A(0.0,-1.0) -> (_ <- B(1.0,0.0) -> _)))", tree.root.toSimplifiedTreeString())
    }

    @Test
    fun testSuccessor() {
        val tree = KDTree2<String>()
        tree.insert(Vector2(1f, 0f), "B")
        tree.insert(Vector2(-3f, 2f), "C")
        tree.insert(Vector2(-3f, 3f), "D")
        tree.insert(Vector2(2f, -4f), "E")
        val f = tree.insert(Vector2(0.01f, -2f), "F")
        tree.insert(Vector2(0.8f, -1f), "G")
        tree.insert(Vector2(0.3f, -5f), "H")
        assertEquals(f, tree.root.successor())
        tree.root.assertInvariant()

        val i = tree.insert(Vector2(-0.001f, -2f), "I")
        assertEquals(i, tree.root.successor())
        tree.root.assertInvariant()
    }

    @Test
    fun testRemoval() {
        val tree = KDTree2<String>()
        tree.insert(Vector2(1f, 0f), "B")
        tree.insert(Vector2(-3f, 2f), "C")
        tree.insert(Vector2(-3f, 3f), "D")
        tree.insert(Vector2(2f, -4f), "E")
        val f = tree.insert(Vector2(0.01f, -2f), "F")
        tree.insert(Vector2(0.8f, -1f), "G")
        tree.insert(Vector2(0.3f, -5f), "H")
        tree.insert(Vector2(-0.001f, -2f), "I")
        f.remove()

        tree.root.assertInvariant()
    }

    @Test
    fun testNearest() {
        run {
            val tree = KDTree2<String>()
            tree.insert(Vector2(0.24710926346272533f, 0.9569330410242274f), "A")
            tree.insert(Vector2(0.2841516430490816f, 0.37508126366009464f), "B")
            val n2 = tree.insert(Vector2(0.390413393177099f, 0.20922686856291806f), "C")
            tree.insert(Vector2(0.7533358947116714f, 0.3222098639265484f), "D")
            tree.insert(Vector2(0.5879956645057056f, 0.4450547928026609f), "E")
            tree.insert(Vector2(0.16577568005593657f, 0.2279176157292686f), "F")
            tree.insert(Vector2(0.28744879869172923f, 0.385568691802207f), "G")
            tree.insert(Vector2(0.2746368346334619f, 0.8605326357051143f), "H")
            tree.insert(Vector2(0.6036515111356207f, 0.5504156254830147f), "I")
            tree.insert(Vector2(0.07803404447950046f, 0.2583798246722264f), "J")
            assertEquals(n2, tree.findNearest(Vector2(0.3945425438172433f, 0.17168490741652354f)).first)
            tree.root.assertInvariant()
        }

        run {
            val tree = KDTree2<String>()
            tree.insert(Vector2(0.3377048165767067f, 0.3257325282311072f), "A")
            tree.insert(Vector2(0.724398827775778f, 0.013407731035484205f), "B")
            tree.insert(Vector2(0.3268588980341771f, 0.8692209246101495f), "C")
            tree.insert(Vector2(0.44378638589767094f, 0.7569000106166637f), "D")
            tree.insert(Vector2(0.8503746691086007f, 0.9605262157426516f), "E")
            val n5 = tree.insert(Vector2(0.46532288668194666f, 0.4128269328874159f), "F")
            tree.insert(Vector2(0.7124535971242029f, 0.5335840009543125f), "G")
            tree.insert(Vector2(0.7192964369092348f, 0.36498760001164476f), "H")
            tree.insert(Vector2(0.9319616217990739f, 0.9834828154873676f), "I")
            tree.insert(Vector2(0.6703646964002569f, 0.9677722849375704f), "J")
            assertEquals(n5, tree.root.findNearest(Vector2(0.4391294037548362f, 0.3192866032025171f)).first)
            tree.root.assertInvariant()
        }

        run {
            val tree = KDTree2<String>()
            tree.insert(Vector2(0.767664758164728f, 0.3285400901814104f), "A")
            tree.insert(Vector2(0.47603802948417695f, 0.6987149242437812f), "B")
            tree.insert(Vector2(0.5214118213038866f, 0.2589234595152985f), "C")
            tree.insert(Vector2(0.15825455063723437f, 0.5196537569794925f), "D")
            tree.insert(Vector2(0.41307461301395665f, 0.08199798419783033f), "E")
            tree.insert(Vector2(0.4576946911327373f, 0.9931330156922431f), "F")
            val n6 = tree.insert(Vector2(0.27126120428168166f, 0.32478855512420124f), "G")
            tree.insert(Vector2(0.038414267996036644f, 0.6419784168230493f), "H")
            tree.insert(Vector2(0.5119108126201046f, 0.8666039042668284f), "I")
            tree.insert(Vector2(0.8524299083246594f, 0.3847424259411648f), "J")
            assertEquals(n6, tree.root.findNearest(Vector2(0.17744822142939498f, 0.24163722948247646f)).first)
            tree.root.assertInvariant()
        }

        run {
            val tree = KDTree2<String>()
            tree.insert(Vector2(0.1442582674977877f, 0.7760322663813887f), "0")
            tree.insert(Vector2(0.032085872324910225f, 0.5694973305098834f), "1")
            tree.insert(Vector2(0.3277901111208089f, 0.677222737006143f), "2")
            tree.insert(Vector2(0.5800902519585343f, 0.051159072835554764f), "3")
            tree.insert(Vector2(0.3234553261376403f, 0.9830955223699145f), "4")
            tree.insert(Vector2(0.8756733921493117f, 0.10085649474571712f), "5")
            tree.insert(Vector2(0.4106769903844528f, 0.8601877159972873f), "6")
            tree.insert(Vector2(0.7346153464154518f, 0.05884418263746949f), "7")
            tree.insert(Vector2(0.5873724052431615f, 0.840055290218239f), "8")
            tree.insert(Vector2(0.9559333963630984f, 0.12596328758470976f), "9")
            tree.insert(Vector2(0.8435954410361118f, 0.8349129522425836f), "10")
            tree.insert(Vector2(0.7843671173698112f, 0.017124638674837933f), "11")
            tree.insert(Vector2(0.879059770515976f, 0.02551251703657631f), "12")
            tree.insert(Vector2(0.24299762183633655f, 0.3362814130032963f), "13")
            tree.insert(Vector2(0.4250686508975735f, 0.5133374684240493f), "14")
            tree.insert(Vector2(0.542214792456234f, 0.9016070541732315f), "15")
            tree.insert(Vector2(0.6157323392061146f, 0.683144670697795f), "16")
            tree.insert(Vector2(0.28838634012495556f, 0.6644510212903654f), "17")
            tree.insert(Vector2(0.9237061395384729f, 0.6290814880505702f), "18")
            tree.insert(Vector2(0.4500983466485362f, 0.1874708031371769f), "19")
            tree.insert(Vector2(0.5982851134572917f, 0.9169721081409393f), "20")
            tree.insert(Vector2(0.5107279491097493f, 0.11302596653865116f), "21")
            tree.insert(Vector2(0.21812602339765153f, 0.3718410659388882f), "22")
            tree.insert(Vector2(0.6560646027510628f, 0.11721731848718664f), "23")
            tree.insert(Vector2(0.8798544146266144f, 0.5442887796130969f), "24")
            tree.insert(Vector2(0.8126523290470522f, 0.29726590345306214f), "25")
            tree.insert(Vector2(0.7828087665039895f, 0.14987510919621538f), "26")
            tree.insert(Vector2(0.7393293870855413f, 0.7710297550164635f), "27")
            tree.insert(Vector2(0.279339314798704f, 0.6274188454590645f), "28")
            tree.insert(Vector2(0.81493405934082f, 0.5848232951968038f), "29")
            tree.insert(Vector2(0.3130275638893253f, 0.4927180902539121f), "30")
            val n31 = tree.insert(Vector2(0.6278082115138979f, 0.21778833660268537f), "31")
            tree.insert(Vector2(0.814317326816526f, 0.09683650503632213f), "32")
            tree.insert(Vector2(0.6830652812675274f, 0.8427379806833216f), "33")
            tree.insert(Vector2(0.2952140333604393f, 0.862757222224029f), "34")
            tree.insert(Vector2(0.7424881456470603f, 0.8580793710673573f), "35")
            tree.insert(Vector2(0.10566246068009477f, 0.7499694626198639f), "36")
            tree.insert(Vector2(0.35253085650994276f, 0.28938203959410735f), "37")
            tree.insert(Vector2(0.49930066997509115f, 0.5235346014136739f), "38")
            tree.insert(Vector2(0.6751151367007803f, 0.5176833304320243f), "39")
            tree.insert(Vector2(0.7237832072646339f, 0.7720280376048032f), "40")
            tree.insert(Vector2(0.017318318829509072f, 0.612604441072542f), "41")
            tree.insert(Vector2(0.7740617896915203f, 0.606197537449164f), "42")
            tree.insert(Vector2(0.8770526137988749f, 0.9790423053581107f), "43")
            tree.insert(Vector2(0.16483567792542286f, 0.43723581161893255f), "44")
            tree.insert(Vector2(0.9986395083556578f, 0.5335422147076112f), "45")
            tree.insert(Vector2(0.907227299426985f, 0.5680992127125262f), "46")
            tree.insert(Vector2(0.37766167441616005f, 0.8724010675629061f), "47")
            tree.insert(Vector2(0.8545082015514659f, 0.4650846697741132f), "48")
            tree.insert(Vector2(0.17373968361217496f, 0.600975191158751f), "49")
            tree.insert(Vector2(0.07137317795106213f, 0.34099867212261714f), "50")
            tree.insert(Vector2(0.24597537643488332f, 0.6024859482636648f), "51")
            tree.insert(Vector2(0.5431485170596755f, 0.4673361015089975f), "52")
            tree.insert(Vector2(0.4482829164064187f, 0.3795640226742327f), "53")
            tree.insert(Vector2(0.7223679418226966f, 0.01032522970375016f), "54")
            tree.insert(Vector2(0.5990969225032046f, 0.03515874185222523f), "55")
            tree.insert(Vector2(0.9818949441908141f, 0.4108339341732876f), "56")
            tree.insert(Vector2(0.9453116121026307f, 0.8796906580376536f), "57")
            tree.insert(Vector2(0.3222624502718304f, 0.1962487058952146f), "58")
            tree.insert(Vector2(0.8808545248761687f, 0.5651829111660612f), "59")
            tree.insert(Vector2(0.07348851710796178f, 0.12065895259835979f), "60")
            tree.insert(Vector2(0.3621222422611112f, 0.9790580798233128f), "61")
            tree.insert(Vector2(0.28840641696384683f, 0.9196314228005439f), "62")
            tree.insert(Vector2(0.08906377337428006f, 0.729872601958318f), "63")
            tree.insert(Vector2(0.4989976083799944f, 0.5701541344049894f), "64")
            tree.insert(Vector2(0.12957333442077756f, 0.06318140342876344f), "65")
            tree.insert(Vector2(0.5714055199595283f, 0.27572528268264573f), "66")
            tree.insert(Vector2(0.5617583965061407f, 0.579569292815256f), "67")
            tree.insert(Vector2(0.5400944126563522f, 0.9239661191317837f), "68")
            tree.insert(Vector2(0.7648866390420962f, 0.8340627442009374f), "69")
            tree.insert(Vector2(0.353845092699136f, 0.6518353649260168f), "70")
            tree.insert(Vector2(0.7062069871184415f, 0.389370285668318f), "71")
            tree.insert(Vector2(0.8746792238664743f, 0.23999317085908578f), "72")
            tree.insert(Vector2(0.8662750123357289f, 0.2501827480824128f), "73")
            tree.insert(Vector2(0.3631925320497791f, 0.8950533017986118f), "74")
            tree.insert(Vector2(0.8346821415556313f, 0.6259099255756083f), "75")
            tree.insert(Vector2(0.3132065040903468f, 0.882578629236276f), "76")
            tree.insert(Vector2(0.2985990255292811f, 0.4965077749707074f), "77")
            tree.insert(Vector2(0.7610521377108704f, 0.5610752897440376f), "78")
            tree.insert(Vector2(0.6795091513732644f, 0.6129027237981436f), "79")
            tree.insert(Vector2(0.6498279675005324f, 0.7914011589802755f), "80")
            tree.insert(Vector2(0.47958824708305325f, 0.9777152780693473f), "81")
            tree.insert(Vector2(0.12480514433662304f, 0.4835343978198331f), "82")
            tree.insert(Vector2(0.7043885892390639f, 0.6679758118342263f), "83")
            tree.insert(Vector2(0.37084726783501065f, 0.9797916114971882f), "84")
            tree.insert(Vector2(0.424393232297267f, 0.3587053703464509f), "85")
            tree.insert(Vector2(0.46549325711675993f, 0.3358322381205284f), "86")
            tree.insert(Vector2(0.3769446644191784f, 0.8067339389098681f), "87")
            tree.insert(Vector2(0.650722572607844f, 0.6112191777838973f), "88")
            tree.insert(Vector2(0.707953508966339f, 0.3195530790580692f), "89")
            tree.insert(Vector2(0.679813959950381f, 0.6846963336958262f), "90")
            tree.insert(Vector2(0.6470825025875696f, 0.6076355662187047f), "91")
            tree.insert(Vector2(0.6928713718624769f, 0.708337311298192f), "92")
            tree.insert(Vector2(0.4297261997274173f, 0.24813678557261376f), "93")
            tree.insert(Vector2(0.09130558074059114f, 0.000261788369922189f), "94")
            tree.insert(Vector2(0.7732801003300176f, 0.45198318156214845f), "95")
            tree.insert(Vector2(0.7253990250823582f, 0.8112947117928974f), "96")
            tree.insert(Vector2(0.7970748506124288f, 0.2869306728067994f), "97")
            tree.insert(Vector2(0.7056568675091179f, 0.36482975988780764f), "98")
            tree.insert(Vector2(0.8115884222128222f, 0.7557123861129647f), "99")
            assertEquals(n31, tree.root.findNearest(Vector2(0.6568752658904902f, 0.18565652448148295f)).first)
            tree.root.assertInvariant()
        }
    }

    @Test
    fun randomTest() {
        val random = Random(0)
        val tree = KDTree2<Int>()
        val nodes = ArrayList<KDTree2Node<Int>>(10000)
        (1..10000).forEach {
            val node = tree.insert(Vector2(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1), it)
            nodes.add(node)
        }
        nodes.shuffle(random)  // Shuffle nodes for removal
        println("Items sorted")
        (1..100).forEach { i ->
            when (random.nextInt(2)) {
                0 -> {  // Remove a random node
                    println("$i: removing")
                    val node = nodes.removeAt(nodes.size - 1)
                    node.remove()
                }
                1 -> {  // Insert an element
                    println("$i: inserting")
                    val node = tree.insert(Vector2(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1), random.nextInt(10000) + 10000)
                    nodes.add(node)
                }
            }
            tree.root.assertInvariant()
        }
    }

}

fun <T> KDTree2Node<T>.assertInvariant() {
    c0?.let {
        if (dimension) {
            assertTrue("$this first child must have x < it", it.key.x < this.key.x)
        } else {
            assertTrue("$this first child must have y < it", it.key.y < this.key.y)
        }
        assertNotEquals(dimension, it.dimension)
        it.assertInvariant()
    }
    c1?.let {
        if (dimension) {
            assertTrue("$this first child must have x >= it", it.key.x >= this.key.x)
        } else {
            assertTrue("$this first child must have y >= it", it.key.y >= this.key.y)
        }
        assertNotEquals(dimension, it.dimension)
        it.assertInvariant()
    }
}