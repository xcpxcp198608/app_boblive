package com.wiatec.boblive.task

import android.content.Intent
import android.content.pm.ResolveInfo
import com.wiatec.boblive.Application
import com.wiatec.boblive.pojo.AppInfo
import com.wiatec.boblive.sql.AppDao

/**
 * Created by patrick on 31/07/2017.
 * create time : 3:44 PM
 */
class LoadInstalledApp: Runnable {
    override fun run() {
        load()
    }

    private fun load() {
        val appDao: AppDao = AppDao()
        val packageManager = Application.context!!.packageManager
        val intent = Intent("android.intent.action.MAIN")
        intent.addCategory("android.intent.category.LAUNCHER")
        val localList: List<ResolveInfo> = packageManager.queryIntentActivities(intent ,0)
        val iterator: Iterator<ResolveInfo>  = localList.iterator()
        while (iterator.hasNext()) {
            val resolveInfo = iterator.next()
            val appInfo = AppInfo()
            appInfo.name = resolveInfo.loadLabel(packageManager).toString()
            appInfo.packageName = resolveInfo.activityInfo.packageName
            //通过包名过滤不需要显示的app
            if("com.android.tv.settings" != appInfo.packageName &&
                    "com.wiatec.boblive" != appInfo.packageName ){
                    appDao.insertOrUpdate(appInfo)
            }
        }
    }

}