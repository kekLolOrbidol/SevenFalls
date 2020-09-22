package com.zavvytech.centerofearth

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.SurfaceHolder
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.zavvytech.centerofearth.features.ColorPref
import com.zavvytech.centerofearth.graphics.ResourceManager
import com.zavvytech.centerofearth.web.FacebookUtills
import com.zavvytech.centerofearth.web.TabsInterface
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_menu.*


class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, SensorEventListener, ResetCallBack {
    private var isRunning = false
        set(value) {
            field = value
            if (value) startGameRunning()
            else stopGameRunning()
        }

    private var gameThread: Thread? = null
    private val endGameTimeout: Long = 3000
    private var surfaceCreated = false

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        ScreenManager.context = this
        ScreenManager.attachResetCallBack(this)
        ResourceManager.resources = resources
        surfaceView.post {
            ScreenManager.setSize(surfaceView.width.toFloat(), surfaceView.height.toFloat())
            ScreenManager.setScreen(ScreenManager.ScreenType.GAME)
        }
        isRunning = true
        surfaceView.holder.addCallback(this)
        surfaceView.setOnTouchListener { _, motionEvent ->
            ScreenManager.onTouch(motionEvent)
            true
        }
        val manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER)[0]
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onDestroy() {
        isRunning = false
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        val color = ColorPref(this).getColor()
        if(color != -1){
            if (color != null) {
                window.statusBarColor = color
            }
        }
    }

    override fun onBackPressed() {
//        if (ScreenManager.onBackPressed()) {
//            super.onBackPressed()
//        }
        super.onBackPressed()
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    private fun stopGameRunning() {
        gameThread?.join(endGameTimeout)
        ScreenManager.disposeAll()
    }

    private fun startGameRunning() {
        gameThread = Thread {
            while (isRunning) {
                if (surfaceCreated) {
                    synchronized(surfaceView.holder) {
                        val canvas = surfaceView.holder.lockCanvas()
                        if (canvas != null) {
                            ScreenManager.gameLoop(canvas)
                            surfaceView.holder.unlockCanvasAndPost(canvas)
                        }
                    }
                }
            }
        }
        gameThread?.start()
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceCreated(holder: SurfaceHolder?) {
        surfaceCreated = true
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        surfaceCreated = false
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            ScreenManager.onChanged(event)
        }
    }

    override fun reset() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        //finish()
    }



}

