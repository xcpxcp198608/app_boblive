package com.wiatec.boblive.presenter

import com.wiatec.boblive.*
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.model.AuthorizationProvider
import com.wiatec.boblive.model.Loadable
import com.wiatec.boblive.model.LoadableWithParams
import com.wiatec.boblive.model.UpgradeProvider
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.pojo.UpgradeInfo
import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.view.Main

/**
 * Created by patrick on 19/06/2017.
 * create time : 3:40 PM
 */
class MainPresenter(val main: Main): BasePresenter<Main>() {

    val upgradeProvider: UpgradeProvider = UpgradeProvider()
    val authorizationProvider: AuthorizationProvider = AuthorizationProvider()

    fun checkUpgrade(){
        upgradeProvider.onLoad(object : Loadable.OnLoadListener<UpgradeInfo>{
            override fun onSuccess(execute: Boolean, t: UpgradeInfo?) {
                if(!execute) {
                    main.checkUpgrade(false, t)
                    return
                }
                val execute1 = AppUtil.isNeedUpdate(Application.context!!, t!!.code)
                main.checkUpgrade(execute1, t)
            }
        })
    }

    fun activeAuthorization(authorization: String){
        authorizationProvider.onLoad(URL_ACTIVE, authorization,
                object : LoadableWithParams.OnLoadListener<ResultInfo<AuthorizationInfo>>{
                    override fun onSuccess(execute: Boolean, t: ResultInfo<AuthorizationInfo>?) {
                        main.activeAuthorization(execute, t)
                    }
                })
    }


    fun validateAuthorization(authorization: String){
        authorizationProvider.onLoad(URL_VALIDATE, authorization,
                object : LoadableWithParams.OnLoadListener<ResultInfo<AuthorizationInfo>>{
                    override fun onSuccess(execute: Boolean, t: ResultInfo<AuthorizationInfo>?) {
                        main.validateAuthorization(execute, t)
                    }
                })
    }
}