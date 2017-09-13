package com.wiatec.boblive.model

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.entity.CODE_OK
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.instance.*
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster

/**
 * IChannel provider
 */
class ChannelProvider : ListLoadableWithParam<ChannelInfo>{

    override fun onLoad(param: String, onLoadListener: ListLoadableWithParam.OnLoadListener<ChannelInfo>) {
        val language: String = SPUtil.get(Application.context!!, KEY_LANGUAGE, LANGUAGE_SK) as String
        val authorization: String = SPUtil.get(Application.context!!, KEY_AUTHORIZATION, "").toString()
        val url = "$URL_CHANNEL$language/$param/$TOKEN"
//        Logger.d(url)
        OkMaster.get(url)
                .enqueue(object: StringListener(){
                    override fun onSuccess(s: String?) {
                        val resultInfo: ResultInfo<ChannelInfo> = Gson().fromJson(s,
                                object : TypeToken<ResultInfo<ChannelInfo>>(){}.type)
                        if(resultInfo.code == CODE_OK){
                            val channelList: ArrayList<ChannelInfo> = resultInfo.data
//                            Logger.d(channelList)
                            if(channelList.size > 0){
                                onLoadListener.onSuccess(true, channelList)
                            }else{
                                onLoadListener.onSuccess(false, null)
                            }
                        }else{
                            onLoadListener.onSuccess(false, null)
                        }
                    }

                    override fun onFailure(e: String?) {
                        onLoadListener.onSuccess(false, null)
                        Logger.d(e!!)
                    }
                })
    }
}