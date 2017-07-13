package com.wiatec.boblive.presenter

import com.wiatec.boblive.Application
import com.wiatec.boblive.URL_ACTIVE
import com.wiatec.boblive.URL_VALIDATE
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.model.AuthorizationProvider
import com.wiatec.boblive.model.Loadable
import com.wiatec.boblive.model.LoadableWithParams
import com.wiatec.boblive.model.UpgradeProvider
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.pojo.UpgradeInfo
import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.view.Splash


class SplashPresenter(val splash: Splash): BasePresenter<Splash>() {

    val upgradeProvider: UpgradeProvider = UpgradeProvider()
    val authorizationProvider: AuthorizationProvider = AuthorizationProvider()

    fun checkUpgrade(){
        upgradeProvider.onLoad(object : Loadable.OnLoadListener<UpgradeInfo>{
            override fun onSuccess(execute: Boolean, t: UpgradeInfo?) {
                if(!execute) {
                    splash.checkUpgrade(false, t)
                    return
                }
                val execute1 = AppUtil.isNeedUpdate(Application.context!!, t!!.code)
                splash.checkUpgrade(execute1, t)
            }
        })
    }

    fun activeAuthorization(authorization: String){
        authorizationProvider.onLoad(URL_ACTIVE, authorization,
                object : LoadableWithParams.OnLoadListener<ResultInfo<AuthorizationInfo>>{
                    override fun onSuccess(execute: Boolean, t: ResultInfo<AuthorizationInfo>?) {
                        splash.activeAuthorization(execute, t)
                    }
                })
    }


    fun validateAuthorization(authorization: String){
        authorizationProvider.onLoad(URL_VALIDATE, authorization,
                object : LoadableWithParams.OnLoadListener<ResultInfo<AuthorizationInfo>>{
                    override fun onSuccess(execute: Boolean, t: ResultInfo<AuthorizationInfo>?) {
                        splash.validateAuthorization(execute, t)
                    }
                })
    }

}