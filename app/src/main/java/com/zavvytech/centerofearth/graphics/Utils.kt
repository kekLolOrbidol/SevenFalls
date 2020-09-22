package com.zavvytech.centerofearth.graphics

import com.zavvytech.centerofearth.ScreenManager
import org.jbox2d.common.Vec2

object Utils {
    const val screenWidthMetres = 20f
    const val blocksAcrossScreen = 7
    const val blockSizeMetres = screenWidthMetres/blocksAcrossScreen

    fun metresToPixels(metres: Float): Float {
        if (ScreenManager.viewport.width() == 0f) return metres

        return ScreenManager.viewport.width() / screenWidthMetres * metres
    }

    fun pixelsToMetres(pixels: Float): Float {
        if (ScreenManager.viewport.width() == 0f) return pixels

        return pixels * screenWidthMetres / ScreenManager.viewport.width()
    }

    fun Vec2.toMetres(): Vec2 {
        this.x = pixelsToMetres(this.x)
        this.y = pixelsToMetres(this.y)
        return this
    }

    fun Vec2.toPixels(): Vec2 {
        this.x = metresToPixels(this.x)
        this.y = metresToPixels(this.y)
        return this
    }
}