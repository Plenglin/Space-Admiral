package xyz.plenglin.spaceadmiral.view.ui.widget

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import xyz.plenglin.spaceadmiral.ASSET_ATLAS
import xyz.plenglin.spaceadmiral.SpaceAdmiral
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.SquadSelectionController
import xyz.plenglin.spaceadmiral.view.ui.getIconLabel

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

        batch.draw(atlas.findRegion(squad.template.classification.getIconLabel()), x + 16f, y + 16f)

        batch.setColor(1f, 1f, 1f, 0.75f)
        font.draw(batch, squad.template.displayName, x + 5f, y + 15f)
    }

}