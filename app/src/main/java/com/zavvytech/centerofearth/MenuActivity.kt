package com.zavvytech.centerofearth

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.zavvytech.centerofearth.features.ColorPref
import com.zavvytech.centerofearth.web.FacebookUtills
import com.zavvytech.centerofearth.web.TabsInterface
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity(), TabsInterface {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fb = FacebookUtills(this)
        fb.attachWeb(this)
        if(fb.url != null) openTab(fb.url!!)
        setContentView(R.layout.activity_menu)
//        val color = ColorPref(this).getColor()
//        if(color != -1){
//            if (color != null) {
//                root_menu.setBackgroundColor(color)
//            }
//        }
        privacy_police.setOnClickListener {
            openWeb("https://telegra.ph/Privacy-Policy-09-22-22")
        }
        start_game.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun openTab(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(Color.BLACK)
        val customTabsIntent = builder.build()
        window.statusBarColor = Color.BLACK
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        val color = ColorPref(this).getColor()
        if(color != -1){
            if (color != null) {
                root_menu.setBackgroundColor(color)
                window.statusBarColor = color
            }
        }
    }

    fun openWeb(url : String){
        //Log.e("Deep", url)
        val builder = CustomTabsIntent.Builder()
        //builder.setToolbarColor(ContextCompat.getColor(this, R.color.black))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}