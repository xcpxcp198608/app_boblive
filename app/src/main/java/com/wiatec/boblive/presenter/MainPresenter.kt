package com.wiatec.boblive.presenter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.HttpMaster
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.TOKEN
import com.wiatec.boblive.URL_CHANNEL
import com.wiatec.boblive.URL_CHANNEL_TYPE
import com.wiatec.boblive.entity.CODE_OK
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import com.wiatec.boblive.view.IMainActivity

/**
 * Created by patrick on 19/06/2017.
 * create time : 3:40 PM
 */
class MainPresenter(val iMainActivity: IMainActivity): BasePresenter<IMainActivity>() {

    fun loadChannelType(){
        HttpMaster(URL_CHANNEL_TYPE + TOKEN).execute(object :HttpMaster.OnLoadListener{
            override fun onSuccess(s: String) {
                val resultInfo:ResultInfo<ChannelTypeInfo> = Gson().fromJson(s, object:TypeToken<ResultInfo<ChannelTypeInfo>>(){}.type)
                if(resultInfo.code == CODE_OK){
                    val channelTypeList: ArrayList<ChannelTypeInfo> = resultInfo.data
                    if(channelTypeList.size > 0)
                    iMainActivity.loadChannelType(channelTypeList)
                }
            }

            override fun onFailure(e: String) {
                Logger.d(e)
            }
        })
    }

    fun loadChannel(country: String){
        OkMaster.get(URL_CHANNEL + country + TOKEN).enqueue(object :StringListener(){
            override fun onSuccess(s: String) {
                val resultInfo:ResultInfo<ChannelInfo> = Gson().fromJson(s, object : TypeToken<ResultInfo<ChannelInfo>>(){}.type)
                if(resultInfo.code == CODE_OK){
                    val channelInfoList:ArrayList<ChannelInfo> = resultInfo.data
                    if(channelInfoList.size > 0){
                        iMainActivity.loadChannel(channelInfoList)
                    }
                }

            }

            override fun onFailure(e: String) {
                Logger.d(e)
            }
        })
    }
}