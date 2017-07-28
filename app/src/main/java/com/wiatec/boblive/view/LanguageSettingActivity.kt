package com.wiatec.boblive.view

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.R
import kotlinx.android.synthetic.main.activity_language_setting.*
import java.util.*

/**
 * Created by patrick on 28/07/2017.
 * create time : 1:59 PM
 */
class LanguageSettingActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_setting)
        btSK.setOnClickListener { setLanguage(Locale("SK", "sk", "")) }
        btCZ.setOnClickListener { setLanguage(Locale("CZ", "cs", "")) }
    }

    private fun setLanguage(locale: Locale) {
//        SPUtil.put(this@LanguageSettingActivity, "isFirstBoot", false)
        try {
            val classActivityManagerNative = Class.forName("android.app.ActivityManagerNative")
            val getDefault = classActivityManagerNative.getDeclaredMethod("getDefault")
            val objIActivityManager = getDefault.invoke(classActivityManagerNative)
            val classIActivityManager = Class.forName("android.app.IActivityManager")
            val getConfiguration = classIActivityManager.getDeclaredMethod("getConfiguration")
            val config = getConfiguration.invoke(objIActivityManager) as Configuration
            config.locale = locale
            val clzParams = arrayOf<Class<*>>(Configuration::class.java)
            val updateConfiguration = classIActivityManager.getDeclaredMethod("updateConfiguration", *clzParams)
            updateConfiguration.invoke(objIActivityManager, config)
        } catch (e: Exception) {
            if(e.message != null) {
                Logger.d(e.message!!)
            }
        }
    }
}