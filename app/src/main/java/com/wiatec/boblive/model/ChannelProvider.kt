package com.wiatec.boblive.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.TOKEN
import com.wiatec.boblive.URL_CHANNEL
import com.wiatec.boblive.entity.CODE_OK
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster

/**
 * channel provider
 */
class ChannelProvider : ListLoadableWithParam<ChannelInfo>{

    override fun onLoad(param: String, onLoadListener: ListLoadableWithParam.OnLoadListener<ChannelInfo>) {
        OkMaster.get(URL_CHANNEL + param + TOKEN)
                .enqueue(object: StringListener(){
                    override fun onSuccess(s: String?) {
                        val resultInfo: ResultInfo<ChannelInfo> = Gson().fromJson(s,
                                object : TypeToken<ResultInfo<ChannelInfo>>(){}.type)
                        if(resultInfo.code == CODE_OK){
                            val channelList: ArrayList<ChannelInfo> = resultInfo.data
                            if(channelList.size > 0){
                                onLoadListener.onSuccess(true, channelList)
                            }else{
                                onLoadListener.onSuccess(false, channelList)
                            }
                        }
                    }

                    override fun onFailure(e: String?) {
                        Logger.d(e!!)
                        onLoadListener.onSuccess(false, null)
                    }
                })
    }
}