package com.wiatec.boblive.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.Application
import com.wiatec.boblive.URL_UPDATE
import com.wiatec.boblive.pojo.UpgradeInfo
import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster

/**
 * Created by patrick on 13/07/2017.
 * create time : 11:14 AM
 */
class UpgradeProvider : Loadable<UpgradeInfo>{

    override fun onLoad(onLoadListener: Loadable.OnLoadListener<UpgradeInfo>) {
        OkMaster.get(URL_UPDATE)
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        val upgradeInfo: UpgradeInfo = Gson().fromJson(s,
                                object : TypeToken<UpgradeInfo>(){}.type)
                        Logger.d(upgradeInfo)
                        val update = AppUtil.isNeedUpdate(Application.context!!, upgradeInfo.code)
                        onLoadListener.onSuccess(update, upgradeInfo)
                    }

                    override fun onFailure(e: String?) {
                        Logger.d(e!!)
                        onLoadListener.onSuccess(false, null)
                    }
                })
    }
}