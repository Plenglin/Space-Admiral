package xyz.plenglin.spaceadmiral.view.ui.widget

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.ASSET_ATLAS
import xyz.plenglin.spaceadmiral.SpaceAdmiral
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.SquadSelectionController

class SquadCard(val parent: SquadSelectionController, var index: Int, val squad: SquadCM, val skin: Skin) : Actor() {

    override fun getWidth(): Float = 64f
    override fun getHeight(): Float = 64f

    private val clickListener = object : ClickListener() {
        override fun clicked(event: InputEvent, x: Float, y: Float) {
            parent.onSquadCardClicked(this@SquadCard)
        }
    }

    init {
        addListener(clickListener)
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        val font = skin.getFont("default-font")
        val atlas = SpaceAdmiral.assets.get(ASSET_ATLAS)
        batch.setColor(1f, 1f, 1f, 1f)

        val assetLabel = if (squad.selected) "squad-card-selected" else "squad-card-unselected"
        batch.draw(atlas.findRegion(assetLabel), x, y)

        batch.setColor(0f, 0f, 0f, 0.75f)
        font.draw(batch, squad.template.displayName, 5f, 5f)
    }

    private companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(SquadCard::class.java)
    }
}