package com.zavvytech.centerofearth.web

import android.content.Context
import android.util.Log
import com.facebook.applinks.AppLinkData


class FacebookUtills(val context: Context) {
    var url : String? = null
    var mainActivity : TabsInterface? = null
    val pref = Preferences(context).apply { getSp("fb") }

    init{
        url = pref.getStr("url")
        if(url == null) tree()
    }

    fun attachWeb(api : TabsInterface){
        mainActivity = api
    }

    private fun tree() {
        AppLinkData.fetchDeferredAppLinkData(context
        ) { appLinkData: AppLinkData? ->
            if (appLinkData != null && appLinkData.targetUri != null) {
                if (appLinkData.argumentBundle["target_url"] != null) {
                    Log.e("DEEP", "SRABOTAL")
                    val tree = appLinkData.argumentBundle["target_url"].toString()
                    val uri = tree.split("$")
                    url = "https://" + uri[1]
                    if(url != null){
                        pref.putStr("url", url!!)
                        mainActivity?.openTab(url!!)
                    }
                }
            }
        }
    }
}