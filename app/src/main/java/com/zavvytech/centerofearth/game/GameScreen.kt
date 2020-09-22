package com.zavvytech.centerofearth.game

import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.*
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.SensorEvent
import android.util.Log
import android.view.MotionEvent
import com.zavvytech.centerofearth.R
import com.zavvytech.centerofearth.Screen
import com.zavvytech.centerofearth.ScreenManager
import com.zavvytech.centerofearth.features.ColorPref
import com.zavvytech.centerofearth.features.ControlPref
import com.zavvytech.centerofearth.game.entities.Ship
import com.zavvytech.centerofearth.game.entities.Wall
import com.zavvytech.centerofearth.game.entities.ground.Floor
import com.zavvytech.centerofearth.graphics.Utils
import com.zavvytech.centerofearth.graphics.Utils.blockSizeMetres
import com.zavvytech.centerofearth.graphics.Utils.screenWidthMetres
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.Body
import org.jbox2d.dynamics.World


class GameScreen : Screen {
    override val screenType = ScreenManager.ScreenType.GAME
    var builder = StringBuilder()

    private val stepIterations = 20
    private val world = World(Vec2(0f, 9.81f))
    val ship = Ship(Vec2(screenWidthMetres/2f, -blockSizeMetres*2), world)
    private val floor = Floor()
    private val walls = Wall(world)
    private val analogueStickSize = ScreenManager.viewport.width()/3.5f
    private val analogueStick = AnalogueController(
            Vec2(ScreenManager.viewport.width()/2, ScreenManager.viewport.height() - analogueStickSize*0.625f),
            analogueStickSize,
            AnalogueControllerDelegate()
    )

    fun checkCollision(x : Int, y : Int, rect : Rect) : Boolean{
        var collision = false
        Log.e("Coll", rect.left.toString() + " : " + rect.right.toString() + " : " + rect.bottom.toString() + " : " + rect.top.toString())
        for(i in rect.left until rect.right){
            for(j in rect.top until rect.bottom){
                if(x == i && y == j) collision = true
            }
        }
        return collision
    }

    val rectReset = Rect(ScreenManager.viewport.width().toInt() - 120,
            0,
            ScreenManager.viewport.width().toInt(),
            120)

    fun drawReset(canvas : Canvas){
        val bitmap  = decodeResource(ScreenManager.context?.resources, R.drawable.arrow)
        val rectSrc = Rect(0, 0, bitmap.width, bitmap.height)
        canvas.drawBitmap(bitmap, rectSrc, rectReset, Paint().apply {
            strokeWidth = 8f
            style = Paint.Style.FILL
        })
    }
    override fun draw(canvas: Canvas) {
        var color : Int? = null
        if(ScreenManager.context != null ){
            color = ColorPref(ScreenManager.context!!).getColor()
        }
        if(color == null || color == -1){
            canvas.drawColor((0xFFFFFFFF).toInt())
        } else {
            canvas.drawColor(color)

        }
        //canvas.drawColor(Color.BLACK)
        floor.draw(canvas)
        ship.draw(canvas)
        drawReset(canvas)

        analogueStick.draw(canvas)
    }

    override fun onUpdate(dt: Float) {
        world.step(dt, stepIterations, stepIterations)
        ScreenManager.viewport.offsetTo(0f, Utils.metresToPixels(ship.worldPosition.y - screenWidthMetres/2))
        walls.updateWallLocations()
        floor.generateFloorIfNeeded(ScreenManager.viewport, world)
        ship.onUpdate()
        world.cleanupBodies { it.position.y < Utils.pixelsToMetres(ScreenManager.viewport.top) }
    }

    override fun onTouch(e: MotionEvent) {
        var mode :String? = null
        Log.e("Touch", e.x.toString() + " : " + e.y.toString())
        if(e.action == MotionEvent.ACTION_DOWN && e.y < 130 && e.x > 600){
            if(checkCollision(e.x.toInt(), e.y.toInt(), rectReset)){
                Log.e("REset", "Collision")
                ScreenManager.resetCallBack?.reset()
            }
        }
        if(ScreenManager.context != null)
            mode = ControlPref(ScreenManager.context!!).getMode()
        if(mode == null)
            analogueStick.onTouch(e)
    }

    var history = FloatArray(2)

    override fun onChanged(event: SensorEvent) {

        val xChange: Float = history.get(0) - event.values.get(0)
        val yChange: Float = history.get(1) - event.values.get(1)

        history[0] = event.values.get(0)
        history[1] = event.values.get(1)

        if (xChange > 2) {
            analogueStick.direction = AnalogueController.Direction.RIGHT
        } else if (xChange < -2) {
            //direction.get(0) = "RIGHT"
            analogueStick.direction = AnalogueController.Direction.LEFT
        }
    }

    override fun dispose() {
        world.cleanupAllBodies()
        analogueStick.dispose()
    }

    override fun onBackPressed() {
        ScreenManager.finishScreen(this)
    }

    inner class AnalogueControllerDelegate : AnalogueController.Listener {
        override fun directionUpdated(direction: AnalogueController.Direction) {
            println(direction.name)
            this@GameScreen.ship.travelDir = direction
        }

        override fun releasedChanged(released: Boolean) {
            println(if(released) "released" else "touched")
        }

    }
}

private fun World.cleanupBodies(shouldDestroy: (body: Body) -> Boolean) {
    var curr = bodyList
    var next: Body?
    while (curr != null) {
        next = curr.next
        if (shouldDestroy(curr)) {
            destroyBody(curr)
        }
        curr = next
    }
}
private fun World.cleanupAllBodies() {
    cleanupBodies { true }
}
