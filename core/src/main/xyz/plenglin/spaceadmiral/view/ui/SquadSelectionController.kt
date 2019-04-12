package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.scene2d.table
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.widget.SquadCard

class SquadSelectionController(val ui: GameUI) {
    private data class SquadButtonVM(val squad: SquadCM, val card: SquadCard)
    private val squadVMs = mutableMapOf<SquadCM, SquadButtonVM>()
    private val cards = mutableListOf<SquadCard>()

    private var lastClickedIndex = 0

    val squadListTable = table {
        ui.client.team.squads.forEachIndexed { i, squad ->
            /*val button = button(style = "toggle") {
                label(squad.template.displayName)
                addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        if (ui.selectedSquads.contains(squad)) {
                            ui.selectedSquads.remove(squad)
                        } else {
                            ui.selectedSquads.add(squad)
                        }
                    }
                })
            }
            squadVMs[squad] = SquadButtonVM(squad, button)*/
            val card = SquadCard(this@SquadSelectionController, i, squad, skin)
            this@table.add(card)
            squadVMs[squad] = SquadButtonVM(squad, card)
            cards.add(card)
            row()
        }
    }

    fun onSquadCardClicked(card: SquadCard) {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            ui.selectedSquads.clear()
            val (a, b) = if (lastClickedIndex < card.index) {
                lastClickedIndex to card.index
            } else {
                card.index to lastClickedIndex
            }
            for (i in a..b) {
                cards[i].squad.selected = true
            }
            onSquadSelectionChange()
            return
        }
        lastClickedIndex = card.index
        if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            ui.selectedSquads.clear()
        }
        card.squad.selected = !card.squad.selected
        onSquadSelectionChange()
    }

    fun onSquadSelectionChange() {
        squadVMs.forEach { _, vm ->
            //vm.card.isChecked = ui.selectedSquads.contains(vm.squad)
        }
        if (ui.selectedSquads.isEmpty()) {
            lastClickedIndex = 0
        }
    }

}