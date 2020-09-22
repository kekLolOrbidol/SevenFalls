package com.zavvytech.centerofearth.game

import android.graphics.Canvas
import android.graphics.RectF
import android.view.MotionEvent
import android.view.MotionEvent.*
import com.zavvytech.centerofearth.R
import com.zavvytech.centerofearth.graphics.BitmapSprite
import org.jbox2d.common.Vec2
import kotlin.properties.Delegates

class AnalogueController(private val canvasCenter: Vec2, canvasSize: Float, private val listener:Listener) {
    enum class Direction { LEFT, DOWN, RIGHT, NONE }

    private val sizeBackground = canvasSize
    private val sizeControl = canvasSize/2
    private val canvasDeadZoneRadius = sizeControl/2

    var direction: Direction by Delegates.observable(Direction.NONE) {
        _, _, newValue -> listener.directionUpdated(newValue)
    }

    private var released: Boolean by Delegates.observable(true) {
        _, _, newValue -> listener.releasedChanged(newValue)
    }

    private val rectBackground = RectF(canvasCenter.x - sizeBackground/2, canvasCenter.y - sizeBackground/2,
            canvasCenter.x + sizeBackground/2, canvasCenter.y + sizeBackground/2)

    private val posOffsets: Map<Direction, Vec2> = mapOf(
            Direction.NONE to Vec2(0f, 0f),
            Direction.DOWN to Vec2(0f, rectBackground.height()/2f),
            Direction.LEFT to Vec2(-rectBackground.width()/2f, 0f),
            Direction.RIGHT to Vec2(rectBackground.width()/2f, 0f)
    )
    private val rectControl = RectF()
        get() {
            field.left = canvasCenter.x - sizeControl/2 + posOffsets.getValue(direction).x
            field.top = canvasCenter.y - sizeControl/2 + posOffsets.getValue(direction).y
            field.right = canvasCenter.x + sizeControl/2 + posOffsets.getValue(direction).x
            field.bottom = canvasCenter.y + sizeControl/2 + posOffsets.getValue(direction).y
            return field
        }

    private val backgroundSprite = BitmapSprite(R.drawable.control_background)
    private val controlSprite = BitmapSprite(R.drawable.control)

    interface Listener {
        fun directionUpdated(direction: Direction)
        fun releasedChanged(released: Boolean)
    }

    fun onTouch(me: MotionEvent?): Boolean {
        when (me?.action) {
            ACTION_DOWN -> {
                released = false
                direction = computeDirection(me.x, me.y)
            }
            ACTION_UP -> {
                released = true
                direction = Direction.NONE
            }
            ACTION_MOVE -> {
                direction = computeDirection(me.x, me.y)
            }
        }
        return true
    }

    private fun computeDirection(touchX: Float, touchY: Float): Direction {
        val relX = touchX - canvasCenter.x
        val relY = touchY - canvasCenter.y
        if (relX*relX + relY*relY <= canvasDeadZoneRadius*canvasDeadZoneRadius) {
            return Direction.NONE
        }
        val leftOrUp = (-relY >= relX)
        val rightOrUp = (relX >= relY)
        if (rightOrUp) {
            if (leftOrUp) {
                return Direction.NONE
            }
            return Direction.RIGHT
        } else {
            if (leftOrUp) {
                return Direction.LEFT
            }
            return Direction.DOWN
        }
    }

    fun draw(canvas: Canvas) {
        backgroundSprite.draw(canvas, rectBackground)
        controlSprite.draw(canvas, rectControl)
    }

    fun dispose() {
        backgroundSprite.dispose()
        controlSprite.dispose()
    }
}
