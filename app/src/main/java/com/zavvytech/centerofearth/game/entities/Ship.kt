package com.zavvytech.centerofearth.game.entities

import com.zavvytech.centerofearth.R
import com.zavvytech.centerofearth.game.AnalogueController
import com.zavvytech.centerofearth.game.AnalogueController.Direction.*
import com.zavvytech.centerofearth.graphics.Utils.blockSizeMetres
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.World

class Ship (initialPosition: Vec2, world: World): Entity(initialPosition, world) {
    override val width: Float = blockSizeMetres * 0.8f
    override val height: Float = width * sprite.height/sprite.width
    override val bodyDef: BodyDef = createBodyDef()
    override val fixtureDef: FixtureDef = createFixtureDef()
    var travelDir: AnalogueController.Direction = NONE
    private val shipMotorStrength = body.mass*5.5f
    private val forces = mapOf(
            NONE to Vec2(),
            LEFT to Vec2(-shipMotorStrength, 0f),
            RIGHT to Vec2(shipMotorStrength, 0f),
            DOWN to Vec2(0f, shipMotorStrength)
    )

    override fun textureResId(): Int {
        return R.drawable.ship
    }

    fun onUpdate() {
        body.applyForceToCenter(forces[travelDir])
    }

    private fun createBodyDef(initialVelocity: Vec2 = Vec2(0f,0f)): BodyDef {
        val bodyDef = BodyDef()

        bodyDef.position = initialPosition
        bodyDef.angle = 0.0f
        bodyDef.linearVelocity = initialVelocity
        bodyDef.angularVelocity = 0.0f
        bodyDef.fixedRotation = true
        bodyDef.active = true
        bodyDef.bullet = false
        bodyDef.allowSleep = true
        bodyDef.gravityScale = 1.0f
        bodyDef.linearDamping = 0.0f
        bodyDef.angularDamping = 0.0f
        bodyDef.userData = ObjectType.SHIP
        bodyDef.type = BodyType.DYNAMIC
        return bodyDef
    }

    private fun createFixtureDef(): FixtureDef {
        val shape = CircleShape()
        shape.radius = width/2f
        shape.m_p.x = width/2f
        shape.m_p.y = width/2f
        val fixtureDef = FixtureDef()
        fixtureDef.shape = shape
        fixtureDef.userData = null
        fixtureDef.friction = 0.35f
        fixtureDef.restitution = 0.1f
        fixtureDef.density = 1f
        fixtureDef.isSensor = false
        return fixtureDef
    }
}