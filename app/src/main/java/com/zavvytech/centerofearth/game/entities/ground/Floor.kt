package com.zavvytech.centerofearth.game.entities.ground

import android.graphics.Canvas
import android.graphics.RectF
import com.zavvytech.centerofearth.graphics.Utils
import com.zavvytech.centerofearth.graphics.Utils.blockSizeMetres
import com.zavvytech.centerofearth.graphics.Utils.blocksAcrossScreen
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.World
import java.util.*

class Floor {
    private val blockList = ArrayList<Ground>()
    private var floorGenerationDepth = 0f
    private val randomiser = Random()

    fun draw(canvas: Canvas) {
        blockList.forEach { it.draw(canvas) }
    }

    fun generateFloorIfNeeded(viewport: RectF, world: World) {
        while (floorGenerationDepth < Utils.pixelsToMetres(viewport.bottom) + blockSizeMetres) {
            for (x in 0 until blocksAcrossScreen) {
                createGround(Vec2(blockSizeMetres * x, floorGenerationDepth), world)?.addToBlockList()
            }
            floorGenerationDepth += blockSizeMetres
        }
    }

    private fun createGround(worldPosition: Vec2, world: World): Ground? {
        return when (randomiser.nextFloat()) {
            in 0f..0.1f -> Dirt(blockSizeMetres, worldPosition, world)
            //in 0.05f..0.1f -> Stone(blockSizeMetres, worldPosition, world)
            else -> null
        }
    }

    private fun Ground.addToBlockList() {
        blockList.add(this)
    }

}
