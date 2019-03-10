import xyz.plenglin.spaceadmiral.view.ui.SquadCommandInputProcessor

class SquadCommandHighlighter(val command: SquadCommandInputProcessor) {

    fun render(delta: Float) {
        when (command.state) {
            is SquadCommandInputProcessor.CommandState.MoveToTransform -> {

            }
        }
    }

}