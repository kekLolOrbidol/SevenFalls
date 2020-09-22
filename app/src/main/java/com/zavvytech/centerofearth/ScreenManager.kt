package com.zavvytech.centerofearth

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.hardware.SensorEvent
import android.view.MotionEvent
import com.zavvytech.centerofearth.game.GameScreen

object ScreenManager {
    enum class ScreenType {
        SPLASH, GAME
    }

    var context : Context? = null
    var resetCallBack : ResetCallBack? = null
    val viewport: RectF = RectF(0f,0f,0f,0f)
    private val screens = ArrayList<Screen>()
    private var lastUpdate = System.currentTimeMillis()

    private val currentScreen: Screen?
        get() = if (screens.size >= 1) screens[screens.size - 1] else null

    fun setSize(width: Float, height: Float) {
        viewport.right = viewport.left + width
        viewport.bottom = viewport.top + height
    }

    fun attachResetCallBack(resetCallBack: ResetCallBack){
        this.resetCallBack = resetCallBack
    }

    fun setScreen(type: ScreenType): Screen {
        val newScreen = typeToScreen(type)
        screens.add(newScreen)
        return newScreen
    }

    fun finishScreen(screenToFinish: Screen) {
        screenToFinish.dispose()
        screens.remove(screenToFinish)
    }

    fun onTouch(motionEvent: MotionEvent) {
        currentScreen?.onTouch(motionEvent)
    }

    fun onChanged(event : SensorEvent){
        currentScreen?.onChanged(event)
    }

    fun gameLoop(canvas: Canvas) {
        val currTime = System.currentTimeMillis()

        currentScreen?.onUpdate((currTime - lastUpdate)/1000f)
        currentScreen?.draw(canvas)

        lastUpdate = currTime
    }

    fun disposeAll() {
        while (currentScreen != null) {
            finishScreen(currentScreen!!)
        }
    }

    /**
     * Returns true if ScreenManager doesn't contain any screens
     */
    fun onBackPressed(): Boolean {
        currentScreen?.onBackPressed()
        return currentScreen == null
    }

    private fun typeToScreen(type: ScreenType): Screen {
        return when(type) {
            ScreenType.GAME -> GameScreen()
            else -> throw UnsupportedOperationException("Unsupported screen type: $type")
        }
    }
}