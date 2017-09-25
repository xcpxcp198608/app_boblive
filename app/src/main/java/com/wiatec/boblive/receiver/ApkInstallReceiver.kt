package com.wiatec.boblive.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.instance.Application
import com.wiatec.boblive.pojo.AppInfo
import com.wiatec.boblive.sql.AppDao
import com.wiatec.boblive.utils.AppUtil

/**
 * Created by patrick on 18/08/2017.
 * create time : 1:25 PM
 */
class ApkInstallReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent == null){
            return
        }
        val appDao = AppDao()
        val appInfo = AppInfo()
        val packageName = intent.data.schemeSpecificPart
        appInfo.packageName = packageName
        appInfo.name = AppUtil.getLabelName(Application.context!!, packageName)
        if (Intent.ACTION_PACKAGE_ADDED == intent.action){
            if("com.android.tv.settings" != appInfo.packageName &&
                    "eu.chainfire.supersu" != appInfo.packageName &&
                    "com.droidlogic.appinstall" != appInfo.packageName &&
                    "com.android.inputmethod.latin" != appInfo.packageName &&
                    "com.droidlogic.readlog" != appInfo.packageName &&
                    "com.wiatec.boblive" != appInfo.packageName ){
                appDao.insertOrUpdate(appInfo)
            }
        }else if (Intent.ACTION_PACKAGE_REMOVED == intent.action){
//            Logger.d("remove")
            appDao.delete(appInfo)
        }
    }
}