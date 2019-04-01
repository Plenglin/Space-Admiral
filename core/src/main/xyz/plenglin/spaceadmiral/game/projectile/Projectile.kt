package xyz.plenglin.spaceadmiral.game.projectile

import com.badlogic.gdx.math.Vector2
import xyz.plenglin.spaceadmiral.game.Sector
import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.game.team.Team
import xyz.plenglin.spaceadmiral.util.Capsule2D
import xyz.plenglin.spaceadmiral.util.KDTree2Node
import xyz.plenglin.spaceadmiral.util.MinMaxRectangle
import java.io.Serializable
import java.util.*


abstract class Projectile(
        /**
         * The current location of the projectile.
         */
        val pos: Vector2,
        /**
         * The sector this projectile is located in
         */
        val sector: Sector,
        /**
         * The velocity of the projectile.
         */
        val velocity: Vector2,
        /**
         * How far the projectile must be from an enemy to trigger an effect.
         */
        val detonationRadius: Float,
        /**
         * The team that created this projectile.
         */
        val team: Team? = null
        ) : Serializable {
    val uuid: UUID = UUID.randomUUID()

    var lastPos = Vector2(pos)

    fun update() {
        lastPos.set(pos)
        pos.add(velocity)
    }

    fun getDetectionBoundingBox(): MinMaxRectangle {
        val left: Float
        val right: Float
        if (lastPos.x < pos.x) {
            left = lastPos.x
            right = pos.x
        } else {
            left = pos.x
            right = lastPos.x
        }

        val bottom: Float
        val top: Float
        if (lastPos.y < pos.y) {
            bottom = lastPos.y
            top = pos.y
        } else {
            bottom = pos.y
            top = lastPos.y
        }

        return MinMaxRectangle(
                left - detonationRadius,
                right - detonationRadius,
                top + detonationRadius,
                bottom + detonationRadius
        )
    }

    fun getDetonationCapsule(): Capsule2D {
        return Capsule2D(pos, lastPos, detonationRadius)
    }

    abstract fun canHit(it: KDTree2Node<Ship>): Boolean
    abstract fun onInteractWith(it: Ship)

}
