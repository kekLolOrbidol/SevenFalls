package com.zavvytech.centerofearth.graphics

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import androidx.annotation.DrawableRes

class BitmapSprite(private val texture: Texture, val paint: Paint? = null): Sprite {
    private val matrix = Matrix()
    val width = texture.width
    val height = texture.height

    constructor(@DrawableRes resId: Int) : this(ResourceManager.getTexture(resId))

    fun draw(canvas: Canvas, dst: RectF, rotationDegrees: Float = 0f) {
        matrix.setRectToRect(texture.rect, dst, Matrix.ScaleToFit.FILL)
        matrix.postRotate(rotationDegrees, dst.centerX(), dst.centerY())
        canvas.drawBitmap(texture.bitmap, matrix, paint)
    }

    fun dispose() {
        texture.dispose()
    }
}