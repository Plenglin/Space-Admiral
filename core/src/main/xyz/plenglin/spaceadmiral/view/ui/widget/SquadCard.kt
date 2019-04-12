package xyz.plenglin.spaceadmiral.view.ui.widget

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import xyz.plenglin.spaceadmiral.ASSET_UI_ATLAS
import xyz.plenglin.spaceadmiral.SpaceAdmiral
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.SquadSelectionController
import xyz.plenglin.spaceadmiral.view.ui.getIconLabel

class SquadCard(val parent: SquadSelectionController, var index: Int, val squad: SquadCM, val skin: Skin) : Group() {

    override fun getWidth(): Float = 96f
    override fun getHeight(): Float = 128f

    private val clickListener = object : ClickListener() {
        override fun clicked(event: InputEvent, x: Float, y: Float) {
            parent.onSquadCardClicked(this@SquadCard)
        }
    }

    private val selectionHalo: Image

    init {
        addListener(clickListener)

        val atlas = SpaceAdmiral.assets.get(ASSET_UI_ATLAS)
        selectionHalo = Image(atlas.findRegion("squad-card-selection-halo")).apply {
            setPosition(0f, 0f)
            isVisible = false
        }

        addActor(Image(atlas.findRegion("squad-card")).apply {
            setPosition(0f, 0f)
        })
        addActor(selectionHalo)
        addActor(Image(atlas.findRegion(squad.template.classification.getIconLabel())).apply {
            setAlign(Align.topRight)
            setPosition(64f, 96f)
        })
        addActor(Group().apply {
            addActor(Label(squad.template.displayName, skin).apply {
                setColor(0f, 0f, 0f, 1f)
            })
            setPosition(0f, 128f)
            rotation = -90f
        })
    }

    fun onSelectionChanged() {
        selectionHalo.isVisible = squad.selected
    }
/*
    override fun draw(batch: Batch, parentAlpha: Float) {
        val atlas = SpaceAdmiral.assets.get(ASSET_UI_ATLAS)
        batch.setColor(1f, 1f, 1f, 1f)

        val assetLabel = if (squad.selected) "squad-card-selected" else "squad-card-unselected"
        batch.draw(atlas.findRegion(assetLabel), x, y)

        batch.draw(atlas.findRegion(squad.template.classification.getIconLabel()), x + 16f, y + 16f)

        super.draw(batch, parentAlpha)
    }
*/
}