package com.zavvytech.centerofearth.game.entities

import com.zavvytech.centerofearth.ScreenManager
import com.zavvytech.centerofearth.graphics.Utils.pixelsToMetres
import com.zavvytech.centerofearth.graphics.Utils.screenWidthMetres
import org.jbox2d.collision.shapes.EdgeShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.BodyType
import org.jbox2d.dynamics.World

class Wall(world: World) {
    private val leftWall = createLeftWall(world)
    private val rightWall = createRightWall(world)
    private val leftWallVec = leftWall.position
    private val rightWallVec = rightWall.position

    init {
        updateWallLocations()
    }

    private fun createRightWall(world: World): Body {
        return createWall(world, screenWidthMetres)
    }

    private fun createLeftWall(world: World): Body {
        return createWall(world, 0f)
    }

    private fun createWall(world: World, xPos: Float): Body {
        val bodyDef = BodyDef()
        bodyDef.type = BodyType.STATIC
        bodyDef.position = Vec2(xPos, pixelsToMetres(ScreenManager.viewport.top))
        val shape = EdgeShape()
        shape.set(
                Vec2(0f, pixelsToMetres(-ScreenManager.viewport.height())),
                Vec2(0f, pixelsToMetres(2 * ScreenManager.viewport.height()))
        )
        val body = world.createBody(bodyDef)
        body.createFixture(shape, 0f)
        return body
    }

    fun updateWallLocations() {
        leftWallVec.y = pixelsToMetres(ScreenManager.viewport.top)
        rightWallVec.y = pixelsToMetres(ScreenManager.viewport.top)
        leftWall.setTransform(leftWallVec, 0f)
        rightWall.setTransform(rightWallVec, 0f)
    }

}
