package com.zavvytech.centerofearth

import android.graphics.Canvas
import android.hardware.SensorEvent
import android.view.MotionEvent

interface Screen {
    val screenType: ScreenManager.ScreenType
    fun draw(canvas: Canvas)
    fun onUpdate(dt: Float)
    fun onTouch(e: MotionEvent)
    fun onChanged(e : SensorEvent)
    fun dispose()
    fun onBackPressed()
}