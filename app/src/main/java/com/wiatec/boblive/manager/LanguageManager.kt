package com.wiatec.boblive.manager

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import com.wiatec.boblive.instance.COUNTRY_CS
import com.wiatec.boblive.instance.COUNTRY_SK
import com.wiatec.boblive.instance.LANGUAGE_CS
import com.wiatec.boblive.instance.LANGUAGE_SK
import java.util.*
import java.lang.reflect.AccessibleObject.setAccessible
import java.lang.reflect.Array.setBoolean



/**
 * Created by patrick on 28/07/2017.
 * create time : 3:57 PM
 */
object LanguageManager{

     fun setLanguage(language: String) {
         val locale: Locale
         if(LANGUAGE_SK == language){
            locale = Locale(LANGUAGE_SK, COUNTRY_SK, "")
         }else if(LANGUAGE_CS == language){
            locale = Locale(LANGUAGE_CS, COUNTRY_CS, "")
         }else{
             locale = Locale.ENGLISH
         }
         realSet1(locale)
    }

    /**
     * use this set under android 7.0 and more
     */
    fun realSet(context: Context, locale: Locale){
        val resources: Resources = context.resources
        val displayMetrics: DisplayMetrics = resources.displayMetrics
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, displayMetrics)
    }

    fun realSet1(locale: Locale){
        val amnClass = Class.forName("android.app.ActivityManagerNative")
        val defaultMethod = amnClass.getMethod("getDefault")
        defaultMethod.isAccessible = true
        val amn = defaultMethod.invoke(amnClass)
        val configuration= amnClass.getMethod("getConfiguration")
        configuration.isAccessible = true
        val config = configuration.invoke(amn) as Configuration
        val configClass = config.javaClass
        val f = configClass.getField("userSetLocale")
        f.setBoolean(config, true)
        config.locale = locale
        val methodUpdateConfiguration = amnClass.getMethod("updateConfiguration", Configuration::class.java)
        methodUpdateConfiguration.isAccessible = true
        methodUpdateConfiguration.invoke(amn, config)
    }
}