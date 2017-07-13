package com.wiatec.boblive.presenter

import com.wiatec.boblive.model.ChannelProvider
import com.wiatec.boblive.model.ListLoadableWithParam
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.view.Channel

/**
 * channel presenter
 */
class ChannelPresenter(val channel: Channel): BasePresenter<Channel>(){

    val channelProvider: ChannelProvider = ChannelProvider()

    fun loadChannel(country: String){
        channelProvider.onLoad(country, object : ListLoadableWithParam.OnLoadListener<ChannelInfo>{
            override fun onSuccess(execute: Boolean, list: ArrayList<ChannelInfo>?) {
                channel.loadChannel(execute, list)
            }
        })
    }
}