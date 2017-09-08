package com.wiatec.boblive.instance

import android.content.Context
import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.manager.LanguageManager
import com.wiatec.boblive.task.DownloadAdImage
import com.wiatec.boblive.task.LoadInstalledApp
import com.wiatec.boblive.task.ValidateAuth
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by patrick on 17/06/2017.
 * create time : 11:54 AM
 */

class Application : android.app.Application() {

    companion object {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        Logger.init("----px----")
        context = applicationContext
        val language:  String = SPUtil.get(this@Application, KEY_LANGUAGE, "sk") as String
        val country:  String = SPUtil.get(this@Application, KEY_COUNTRY, "SK") as String
        LanguageManager.setLanguage(language, country)
        val executorService:ExecutorService = Executors.newCachedThreadPool()
        executorService.execute(ValidateAuth())
        executorService.execute(DownloadAdImage())
        executorService.execute(LoadInstalledApp())
    }


}
