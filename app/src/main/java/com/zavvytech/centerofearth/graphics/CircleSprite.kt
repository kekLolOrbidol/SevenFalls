package com.zavvytech.centerofearth.graphics

import android.graphics.Canvas
import android.graphics.Paint
import org.jbox2d.common.Vec2

class CircleSprite(var paint: Paint, var radius: Float): Sprite {

    fun draw(canvas: Canvas, position: Vec2, scale: Float = 1f) {
        canvas.drawCircle(position.x, position.y, radius*scale, paint)
    }

}