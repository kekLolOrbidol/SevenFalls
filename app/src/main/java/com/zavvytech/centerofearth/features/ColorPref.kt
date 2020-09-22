package com.zavvytech.centerofearth.features

import android.content.Context
import android.content.SharedPreferences

class ColorPref(context: Context) {
    private val ACTION_NAME = "color"
    private val colorPref = SPref(context).apply { getSp("color") }

    fun getColor() : Int? {
        return colorPref.getInt(ACTION_NAME)
    }

    fun setColor(color : Int){
        colorPref.putInt(ACTION_NAME, color)
    }
}