package com.zavvytech.centerofearth.features

import android.content.Context

class ControlPref(context: Context) {
    private val ACTION_NAME = "control"
    private val colorPref = SPref(context).apply { getSp(ACTION_NAME) }

    fun getMode() : String? {
        return colorPref.getStr(ACTION_NAME)
    }

    fun setMode(mode : String){
        colorPref.putStr(ACTION_NAME, mode)
    }
}