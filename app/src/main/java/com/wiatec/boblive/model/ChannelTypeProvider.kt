package com.wiatec.boblive.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.instance.TOKEN
import com.wiatec.boblive.instance.URL_CHANNEL_TYPE
import com.wiatec.boblive.entity.CODE_OK
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster

/**
 * Created by patrick on 13/07/2017.
 * create time : 10:52 AM
 */
class ChannelTypeProvider : ListLoadable<ChannelTypeInfo> {

    override fun onLoad(onLoadListener: ListLoadable.OnLoadListener<ChannelTypeInfo>) {
        OkMaster.get(URL_CHANNEL_TYPE + TOKEN)
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        val resultInfo: ResultInfo<ChannelTypeInfo> = Gson().fromJson(s,
                                object: TypeToken<ResultInfo<ChannelTypeInfo>>(){}.type)
                        if(resultInfo.code == CODE_OK){
                            val channelTypeList: ArrayList<ChannelTypeInfo> = resultInfo.data
                            if(channelTypeList.size > 0){
                                onLoadListener.onSuccess(true, channelTypeList)
                            }else{
                                onLoadListener.onSuccess(false, channelTypeList)
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