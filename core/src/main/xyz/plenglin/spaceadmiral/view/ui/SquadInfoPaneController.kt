package xyz.plenglin.spaceadmiral.view.ui

import ktx.scene2d.label
import ktx.scene2d.table

class SquadInfoPaneController(val ui: GameUI) {

    val table = table {
    }

    init {
        onSelectionChange()
    }

    fun onSelectionChange() {
        val selection = ui.selectedSquads
        val size = selection.size

        table.clearChildren()
        when (size) {
            0 -> table.apply {
                label("No selection")
            }
            1 -> table.apply {
                label(selection.first().displayName)
            }
            else -> table.apply {
                label("$size squads")
            }
        }

        table.pack()
    }
}