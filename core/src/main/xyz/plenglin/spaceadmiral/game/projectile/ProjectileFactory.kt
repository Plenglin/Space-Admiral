package xyz.plenglin.spaceadmiral.game.projectile

import xyz.plenglin.spaceadmiral.game.ship.Ship
import xyz.plenglin.spaceadmiral.util.Transform2D

interface ProjectileFactory {
    fun createProjectile(trs: Transform2D, target: Ship): Projectile
}