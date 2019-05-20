package xyz.plenglin.spaceadmiral.view.model

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.ShipID
import xyz.plenglin.spaceadmiral.ShipSubID
import xyz.plenglin.spaceadmiral.SquadID
import xyz.plenglin.spaceadmiral.game.action.ActionCM
import xyz.plenglin.spaceadmiral.game.squad.ShipType
import xyz.plenglin.spaceadmiral.game.squad.SquadTransform
import xyz.plenglin.spaceadmiral.net.game.io.s2c.update.SquadUDTO
import xyz.plenglin.spaceadmiral.util.Transform2D
import xyz.plenglin.spaceadmiral.view.ui.GameUI
import xyz.plenglin.spaceadmiral.view.ui.command.JumpSquadCommand
import xyz.plenglin.spaceadmiral.view.ui.command.SquadCommand
import xyz.plenglin.spaceadmiral.view.ui.getIconLabel

class SquadCM constructor(val uuid: SquadID, val team: TeamCM, val template: ShipType, val transform: SquadTransform) {

    val ui: GameUI? get() = gameState.ui
    val displayName: String get() = template.displayName
    val icon: TextureRegion? get() = ui?.uiAtlas?.findRegion(template.classification.getIconLabel())
    //val texture: TextureRegion? get() = ui?.gameAtlas?.findRegion(template.classification.getIconLabel())

    var selected
        get() = ui!!.selectedSquads.contains(this)
        set(value) {
            val ui = ui!!
            if (value) {
                ui.selectedSquads.add(this)
            } else {
                ui.selectedSquads.remove(this)
            }
        }

    var visible = false

    var sector: SectorCM? = null
    var index: Int = 0

    private val shipMap: HashMap<ShipSubID, ShipCM> = HashMap()
    val ships get() = shipMap.values

    val sendableCommands: List<SquadCommand> = mutableListOf(JumpSquadCommand)
    val queuedActions = mutableListOf<ActionCM>()

    val gameState: GameStateCM get() = team.gameState
    val centerOfMass: Vector2 get() {
        val out = Vector2(0f, 0f)
        for (ship in ships) {
            out.add(ship.transform.posGlobal)
        }
        return out.scl(1f / ships.size)
    }

    fun updateWith(dto: SquadUDTO) {
        visible = true
        queuedActions.clear()
        queuedActions.addAll(dto.actions.map { it.deserialize(gameState) })
    }

    override fun toString(): String {
        return "SquadCM($uuid)"
    }

    operator fun get(shipID: ShipID): ShipCM? {
        if (uuid != shipID.squad) return null
        return shipMap[shipID.ship]
    }

    operator fun get(shipID: ShipSubID): ShipCM? {
        return shipMap[shipID]
    }

    fun addShip(uuid: ShipID, transform: Transform2D): ShipCM {
        val cmShip = ShipCM(
                uuid,
                this,
                transform.toGlobal()
        )
        shipMap[uuid.ship] = cmShip
        return cmShip
    }

}
