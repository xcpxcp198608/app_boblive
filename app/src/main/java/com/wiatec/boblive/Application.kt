package com.wiatec.boblive

import android.content.Context
import com.px.kotlin.utils.Logger
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
        val executorService:ExecutorService = Executors.newCachedThreadPool()
        executorService.execute(ValidateAuth())
    }


}
