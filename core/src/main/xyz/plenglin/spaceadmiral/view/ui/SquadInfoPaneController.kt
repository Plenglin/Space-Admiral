package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.actors.onClick
import ktx.scene2d.button
import ktx.scene2d.label
import ktx.scene2d.table

class SquadInfoPaneController(val ui: GameUI) {

    val table = Table(ui.skin)

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
            add(Label(selectionLabel, skin))
            if (selection.isEmpty()) {
                return
            }

            row()

            var actions = selection.first().sendableCommands.toSet()
            selection.drop(1).forEach {
                actions = actions.intersect(it.sendableCommands)
            }

            add(table {
                actions.forEach { action ->
                    button {
                        label(action.displayName)
                        onClick {
                            ui.squadCommand.performSquadAction(action)
                        }
                    }
                }
            })
            pack()
        }
    }
}