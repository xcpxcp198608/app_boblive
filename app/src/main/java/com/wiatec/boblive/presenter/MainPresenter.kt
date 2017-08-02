package com.wiatec.boblive.presenter

import com.wiatec.boblive.*
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.model.*
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.pojo.UpgradeInfo
import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.view.IMain

/**
 * Created by patrick on 19/06/2017.
 * create time : 3:40 PM
 */
class MainPresenter(val iMain: IMain): BasePresenter<IMain>() {

    val upgradeProvider: UpgradeProvider = UpgradeProvider()
    val authorizationProvider: AuthorizationProvider = AuthorizationProvider()
    val channelTypeProvider: ChannelTypeProvider = ChannelTypeProvider()
    val adImageProvider: AdImageProvider = AdImageProvider()

    fun checkUpgrade(){
        upgradeProvider.onLoad(object : Loadable.OnLoadListener<UpgradeInfo>{
            override fun onSuccess(execute: Boolean, t: UpgradeInfo?) {
                if(!execute) {
                    iMain.checkUpgrade(false, t)
                    return
                }
                val execute1 = AppUtil.isNeedUpdate(Application.context!!, t!!.code)
                iMain.checkUpgrade(execute1, t)
            }
        })
    }

    fun activeAuthorization(authorization: String){
        authorizationProvider.onLoad(URL_ACTIVE, authorization,
                object : LoadableWithParams.OnLoadListener<ResultInfo<AuthorizationInfo>>{
                    override fun onSuccess(execute: Boolean, t: ResultInfo<AuthorizationInfo>?) {
                        iMain.activeAuthorization(execute, t)
                    }
                })
    }


    fun validateAuthorization(authorization: String){
        authorizationProvider.onLoad(URL_VALIDATE, authorization,
                object : LoadableWithParams.OnLoadListener<ResultInfo<AuthorizationInfo>>{
                    override fun onSuccess(execute: Boolean, t: ResultInfo<AuthorizationInfo>?) {
                        iMain.validateAuthorization(execute, t)
                    }
                })
    }

    fun loadAdImage(){
        adImageProvider.onLoad(object: Loadable.OnLoadListener<String>{
            override fun onSuccess(execute: Boolean, t: String?) {
                iMain.loadAdImage(execute, t)
            }
        })
    }
}