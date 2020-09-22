package com.zavvytech.centerofearth.game.entities.ground

import com.zavvytech.centerofearth.game.entities.Entity
import com.zavvytech.centerofearth.game.entities.ObjectType
import org.jbox2d.collision.shapes.PolygonShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.World

abstract class Ground(blockSize: Float, initialPosition: Vec2, world: World): Entity(initialPosition, world) {
    final override val width: Float = blockSize
    final override val height: Float = blockSize
    override val bodyDef: BodyDef = createBodyDef()
    override val fixtureDef: FixtureDef = createFixtureDef()

    private fun createBodyDef(initialVelocity: Vec2 = Vec2(0f,0f)): BodyDef {
        val bodyDef = BodyDef()

        bodyDef.position = initialPosition
        bodyDef.angle = 0.0f
        bodyDef.linearVelocity = initialVelocity
        bodyDef.angularVelocity = 0.0f
        bodyDef.fixedRotation = false
        bodyDef.active = true
        bodyDef.bullet = false
        bodyDef.allowSleep = true
        bodyDef.gravityScale = 1.0f
        bodyDef.linearDamping = 0.0f
        bodyDef.angularDamping = 0.0f
        bodyDef.userData = ObjectType.GROUND
        bodyDef.type = BodyType.STATIC
        return bodyDef
    }

    private fun createFixtureDef(): FixtureDef {
        val shape = PolygonShape()
        shape.setAsBox(width/2, height/2, Vec2(width/2, height/2), 0f)
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.userData = null
        fixtureDef.friction = 0.35f
        fixtureDef.restitution = 0.1f
        fixtureDef.density = 1f
        fixtureDef.isSensor = false
        return fixtureDef
    }

    abstract fun onMine()
    abstract fun groundTypeProbability(depthMetres: Float): Float

}