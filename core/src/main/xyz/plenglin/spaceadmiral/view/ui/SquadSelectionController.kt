package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.scene2d.table
import org.slf4j.LoggerFactory
import xyz.plenglin.spaceadmiral.view.model.SquadCM
import xyz.plenglin.spaceadmiral.view.ui.widget.SquadCard

class SquadSelectionController(val ui: GameUI) {
    private data class SquadButtonVM(val squad: SquadCM, val card: SquadCard)
    private val squadVMs = mutableMapOf<SquadCM, SquadButtonVM>()
    private val cards = mutableListOf<SquadCard>()

    private var lastClickedIndex = 0

    val squadListTable = table {
    }

    fun updateSquadListTable() {
        squadListTable.clearChildren()
        ui.client.team.squads.forEachIndexed { i, squad ->
            val card = SquadCard(this@SquadSelectionController, i, squad, squadListTable.skin)
            squadListTable.add(card).pad(5f)
            squadVMs[squad] = SquadButtonVM(squad, card)
            cards.add(card)
        }
        squadListTable.pack()
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
            vm.card.onSelectionChanged()
        }
        if (ui.selectedSquads.isEmpty()) {
            lastClickedIndex = 0
        }
    }

    private companion object {
        @JvmStatic
        val logger = LoggerFactory.getLogger(SquadCard::class.java)
    }

}