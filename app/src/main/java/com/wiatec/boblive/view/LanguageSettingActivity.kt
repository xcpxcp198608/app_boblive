package com.wiatec.boblive.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.*
import com.wiatec.boblive.manager.LanguageManager
import kotlinx.android.synthetic.main.activity_language_setting.*

/**
 * Created by patrick on 28/07/2017.
 * create time : 1:59 PM
 */
class LanguageSettingActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_setting)
        btSK.setOnClickListener { onClick(LANGUAGE_SK) }
        btCZ.setOnClickListener { onClick(LANGUAGE_CS) }
    }

    private fun onClick(language: String){
        setCache(language)
        LanguageManager.setLanguage(this@LanguageSettingActivity, language)
        val intent: Intent =  Intent(this@LanguageSettingActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
//        // 杀掉进程
//        android.os.Process.killProcess(android.os.Process.myPid())
//        System.exit(0)
    }

    private fun setCache(language: String){
        SPUtil.put(this@LanguageSettingActivity, KEY_FIRST_BOOT, false)
        SPUtil.put(this@LanguageSettingActivity, KEY_LANGUAGE, language)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_BACK || event.keyCode == KeyEvent.KEYCODE_HOME){
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}