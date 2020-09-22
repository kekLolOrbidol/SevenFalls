package com.zavvytech.centerofearth.game.entities

import android.graphics.Canvas
import android.graphics.RectF
import androidx.annotation.DrawableRes
import com.zavvytech.centerofearth.ScreenManager
import com.zavvytech.centerofearth.graphics.BitmapSprite
import com.zavvytech.centerofearth.graphics.ResourceManager
import com.zavvytech.centerofearth.graphics.Utils
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.BodyDef
import org.jbox2d.dynamics.FixtureDef
import org.jbox2d.dynamics.World

abstract class Entity(val initialPosition: Vec2, private val world: World) {
    protected abstract val bodyDef: BodyDef
    protected abstract val fixtureDef: FixtureDef
    protected abstract val width: Float
    protected abstract val height: Float
    protected val body: Body by lazy {
        val body: Body = world.createBody(bodyDef)
        body.createFixture(fixtureDef)
        body
    }
    protected val sprite: BitmapSprite by lazy {
        BitmapSprite(ResourceManager.getTexture(textureResId()))
    }
    var worldPosition = initialPosition
        get() = body.position
    private val canvasPosition: RectF = RectF()
        get() {
            field.left = Utils.metresToPixels(worldPosition.x) - ScreenManager.viewport.left
            field.top = Utils.metresToPixels(worldPosition.y) - ScreenManager.viewport.top
            field.right = field.left + Utils.metresToPixels(width)
            field.bottom = field.top + Utils.metresToPixels(height)
            return field
        }

    @DrawableRes abstract fun textureResId(): Int

    fun draw(canvas: Canvas) {
        sprite.draw(canvas, canvasPosition, body.angle)
    }
}