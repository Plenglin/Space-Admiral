package xyz.plenglin.spaceadmiral.view.ui

import ktx.actors.onClick
import ktx.scene2d.button
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
        val selectionLabel = when (size) {
            0 -> "No selection"
            1 -> selection.first().displayName
            else -> "$size squads"
        }

        table.apply {
            label(selectionLabel)
            if (selection.isEmpty()) {
                return
            }

            row()

            var actions = selection.first().sendableCommands.toSet()
            selection.drop(1).forEach {
                actions = actions.intersect(it.sendableCommands)
            }

            table {
                actions.forEach { action ->
                    button {
                        label(action.displayName)
                        onClick {
                            ui.squadCommand.performSquadAction(action)
                        }
                    }
                }
            }
            pack()
        }
    }
}