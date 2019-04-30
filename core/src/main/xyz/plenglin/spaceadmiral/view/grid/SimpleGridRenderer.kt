package xyz.plenglin.spaceadmiral.view.grid

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import xyz.plenglin.spaceadmiral.SpaceAdmiral.Companion.GRID_SIZE
import xyz.plenglin.spaceadmiral.util.IntVector2
import xyz.plenglin.spaceadmiral.util.toIntVector2
import xyz.plenglin.spaceadmiral.util.unproject2
import xyz.plenglin.spaceadmiral.view.model.GameStateCM

class SimpleGridRenderer : GridRenderer {

    private lateinit var gridCamera: OrthographicCamera
    private lateinit var uiCamera: OrthographicCamera

    private val gridPixmap = Pixmap(GRID_SIZE, GRID_SIZE, Pixmap.Format.RGBA8888)
    private val tex = Texture(gridPixmap)

    private val batch = SpriteBatch()
    private val shape = ShapeRenderer()

    override fun initialize(gameCamera: OrthographicCamera, uiCamera: OrthographicCamera) {
        this.gridCamera = gameCamera
        this.uiCamera = uiCamera
    }

    override fun draw(gs: GameStateCM) {
        gridPixmap.setColor(Color.CLEAR)
        gridPixmap.blending = Pixmap.Blending.None
        gridPixmap.fill()
        gridPixmap.blending = Pixmap.Blending.SourceOver
        gs.tadar.drawTadar(gridPixmap)
        gs.sectors.forEach { p, s ->
            gridPixmap.setColor(contested)
            gridPixmap.drawPixel(p.x, GRID_SIZE - p.y - 1)
        }

        tex.draw(gridPixmap, 0, 0)

        batch.projectionMatrix = gridCamera.combined
        shape.projectionMatrix = gridCamera.combined

        shape.color = Color.WHITE
        //shape.begin(ShapeRenderer.ShapeType.Line)
        //shape.circle(0f, 0f, GRID_SIZE.toFloat())
        //shape.end()

        batch.begin()
        batch.draw(tex, 0f, 0f)
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun getSectorAtScreenPos(x: Int, y: Int): IntVector2? {
        val pos = gridCamera.unproject2(x.toFloat(), y.toFloat()).toIntVector2()
        return if ((0 until GRID_SIZE).contains(pos.x) && (0 until GRID_SIZE).contains(pos.y)) {
            pos
        } else {
            null
        }
    }

    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        @JvmStatic
        val friendly = Color(0f, 0f, 1f, 1f)
        @JvmStatic
        val enemy = Color(1f, 0f, 0f, 1f)
        @JvmStatic
        val contested = Color(1f, 1f, 1f, 1f)
    }
}