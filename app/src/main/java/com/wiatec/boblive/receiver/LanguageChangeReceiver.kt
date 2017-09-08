package com.wiatec.boblive.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.instance.KEY_COUNTRY
import com.wiatec.boblive.instance.KEY_LANGUAGE
import com.wiatec.boblive.manager.LanguageManager
import java.util.*

/**
 * Created by patrick on 08/09/2017.
 * create time : 11:13 AM
 */
class LanguageChangeReceiver: BroadcastReceiver(){

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent == null) return
        if(intent.action == Intent.ACTION_LOCALE_CHANGED){
            if(context == null) return
            val currentLanguage = context.resources.configuration.locale.language
            val currentCountry = context.resources.configuration.locale.country
            Logger.d(currentLanguage + "/" + currentCountry)
            LanguageManager.setLanguage(currentLanguage, currentCountry)
            SPUtil.put(context, KEY_LANGUAGE, currentLanguage)
            SPUtil.put(context, KEY_COUNTRY, currentCountry)
        }
    }
}