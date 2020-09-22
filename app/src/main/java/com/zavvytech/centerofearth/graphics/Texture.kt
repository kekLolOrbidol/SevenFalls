package com.zavvytech.centerofearth.graphics

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.RectF
import androidx.annotation.DrawableRes

class Texture(@DrawableRes private val resId: Int, res: Resources){
    val bitmap = BitmapFactory.decodeResource(res, resId)
    val width = bitmap.width.toFloat()
    val height = bitmap.height.toFloat()
    val rect = RectF(0f, 0f, width, height)

    fun dispose() {
        ResourceManager.notifyDisposed(resId)
    }
}
