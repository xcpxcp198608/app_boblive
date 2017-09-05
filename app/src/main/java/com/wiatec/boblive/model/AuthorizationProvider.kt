package com.wiatec.boblive.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.instance.KEY_KEY
import com.wiatec.boblive.instance.KEY_MAC
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import com.wiatec.boblive.utils.SysUtil

/**
 * Created by patrick on 13/07/2017.
 * create time : 11:20 AM
 */
class AuthorizationProvider : LoadableWithParams<ResultInfo<AuthorizationInfo>>{
    override fun onLoad(url: String, authorization: String,
                        onLoadListener: LoadableWithParams.OnLoadListener<ResultInfo<AuthorizationInfo>>) {
        OkMaster.post(url)
                .parames(KEY_KEY, authorization)
                .parames(KEY_MAC, SysUtil.getEthernetMac())
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        val resultInfo:ResultInfo<AuthorizationInfo> = Gson().fromJson(s,
                                    object : TypeToken<ResultInfo<AuthorizationInfo>>(){}.type)
                        onLoadListener.onSuccess(true, resultInfo)
                    }

                    override fun onFailure(e: String?) {
                        Logger.d(e!!)
                        onLoadListener.onSuccess(false, null)
                    }
                })
    }
}