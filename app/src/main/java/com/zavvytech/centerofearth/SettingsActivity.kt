package com.zavvytech.centerofearth

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jaredrummler.android.colorpicker.ColorShape
import com.zavvytech.centerofearth.features.ColorPref
import com.zavvytech.centerofearth.features.ControlPref
import kotlinx.android.synthetic.main.activity_menu.*

import kotlinx.android.synthetic.main.activity_settings.*

    class SettingsActivity : AppCompatActivity(), ColorPickerDialogListener {

    var controlPref : ControlPref? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val color = ColorPref(this).getColor()
        if(color != -1){
            if (color != null) {
                root_settings.setBackgroundColor(color)
                window.statusBarColor = color
            }
        }
        controlPref = ControlPref(this)
        color_pick.setOnClickListener{
            createColorPickerDialog(0)
        }
        val mode = controlPref?.getMode()
        if(mode == "accel")
            radio_group.check(acc.id)
        if(mode == "controller")
            radio_group.check(controller.id)
    }

    override fun onStop() {
        super.onStop()
        when(radio_group.checkedRadioButtonId){
            acc.id -> controlPref?.setMode("accel")
            controller.id -> controlPref?.setMode("controller")
        }
    }

    private fun createColorPickerDialog(id: Int) {
        ColorPickerDialog.newBuilder()
                .setColor(Color.RED)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowCustom(true)
                .setAllowPresets(true)
                .setColorShape(ColorShape.SQUARE)
                .setDialogId(id)
                .show(this)
    }

    override fun onDialogDismissed(dialogId: Int) {
        Toast.makeText(this, "Dialog dismissed", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onColorSelected(dialogId: Int, color: Int) {
        ColorPref(this).setColor(color)
        root_settings.setBackgroundColor(color)
        window.statusBarColor = color
    }
}