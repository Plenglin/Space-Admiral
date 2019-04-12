package xyz.plenglin.spaceadmiral.view.ui

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import ktx.scene2d.button
import ktx.scene2d.label
import ktx.scene2d.table
import xyz.plenglin.spaceadmiral.view.model.SquadCM

class SquadSelectionController(val ui: GameUI) {
    private data class SquadButtonVM(val squad: SquadCM, val button: Button)
    private val squadVMs = mutableMapOf<SquadCM, SquadButtonVM>()

    val squadListTable = table {
        ui.client.team.squads.forEach { squad ->
            val button = button(style = "toggle") {
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
            squadVMs[squad] = SquadButtonVM(squad, button)
            row()
        }
    }

    fun onSquadSelectionChange() {
        squadVMs.forEach { _, vm ->
            vm.button.isChecked = ui.selectedSquads.contains(vm.squad)
        }
    }

}