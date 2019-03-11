import xyz.plenglin.spaceadmiral.view.ui.SquadCommandInputProcessor

class SquadCommandHighlighter(val command: SquadCommandInputProcessor) {

    fun render(delta: Float) {
        val state = command.state
        when (state) {
            is SquadCommandInputProcessor.CommandState.MoveToTransform -> {
                state.recipients.forEach {

                }
            }
        }
    }

}