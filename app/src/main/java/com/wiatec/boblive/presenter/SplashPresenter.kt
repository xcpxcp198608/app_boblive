package com.wiatec.boblive.presenter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.HttpMaster
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.Application
import com.wiatec.boblive.URL_UPDATE
import com.wiatec.boblive.pojo.UpgradeInfo
import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.view.ISplashActivity


class SplashPresenter(val iSplashActivity: ISplashActivity): BasePresenter<ISplashActivity>() {

    fun checkUpgrade(){
        HttpMaster(URL_UPDATE).execute(object: HttpMaster.OnLoadListener{
            override fun onSuccess(s: String) {
                val upgradeInfo: UpgradeInfo = Gson().fromJson(s, object :TypeToken<UpgradeInfo>(){}.type)
                Logger.d(upgradeInfo)
                val update = AppUtil.isNeedUpdate(Application.context!!, upgradeInfo.code)
                iSplashActivity.checkUpgrade(update, upgradeInfo)
            }

            override fun onFailure(e: String) {
                iSplashActivity.checkUpgrade(false, null!!)
            }
        })
    }
}