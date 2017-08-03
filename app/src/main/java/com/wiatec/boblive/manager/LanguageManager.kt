package com.wiatec.boblive.manager

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import com.wiatec.boblive.COUNTRY_CS
import com.wiatec.boblive.COUNTRY_SK
import com.wiatec.boblive.LANGUAGE_CS
import com.wiatec.boblive.LANGUAGE_SK
import java.util.*

/**
 * Created by patrick on 28/07/2017.
 * create time : 3:57 PM
 */
object LanguageManager{

     fun setLanguage(context: Context, language: String) {
         val locale: Locale
         if(LANGUAGE_SK == language){
            locale = Locale(LANGUAGE_SK, COUNTRY_SK, "")
         }else if(LANGUAGE_CS == language){
            locale = Locale(LANGUAGE_CS, COUNTRY_CS, "")
         }else{
             locale = Locale.ENGLISH
         }
         val resources: Resources = context.resources
         val displayMetrics: DisplayMetrics = resources.displayMetrics
         val configuration: Configuration = resources.configuration
         configuration.locale = locale
         resources.updateConfiguration(configuration, displayMetrics)
    }
}