package xyz.plenglin.spaceadmiral.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import xyz.plenglin.spaceadmiral.SpaceAdmiral.GRID_SIZE
import xyz.plenglin.spaceadmiral.game.ship.Ship
import java.io.Serializable
import java.util.*

class TadarData : Serializable {

    val tadarSignals = Array(GRID_SIZE) { FloatArray(GRID_SIZE) }
    val knownShips = HashMap<UUID, Ship>()

    fun initializeNoise() {
        for (x in 0 until GRID_SIZE) {
            for (y in 0 until GRID_SIZE) {
                tadarSignals[x][y] = NOISE_STD * random.nextGaussian().toFloat() + NOISE_MEAN
            }
        }
    }

    fun drawTadar(pixmap: Pixmap) {
        val color = Color(1f, 0f, 0f, 0f)
        for (x in 0 until GRID_SIZE) {
            for (y in 0 until GRID_SIZE) {
                color.a = tadarSignals[x][y].coerceIn(0f, 1f)
                pixmap.setColor(color)
                pixmap.drawPixel(x, GRID_SIZE - y - 1)
            }
        }
    }

    private companion object {
        const val NOISE_MEAN = 0.3f
        const val NOISE_STD = 0.05f

        @JvmStatic
        val random = Random()
    }

}