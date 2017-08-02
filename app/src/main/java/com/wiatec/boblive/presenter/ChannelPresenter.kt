package com.wiatec.boblive.presenter

import com.wiatec.boblive.model.ChannelProvider
import com.wiatec.boblive.model.ListLoadableWithParam
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.view.IChannel

/**
 * IChannel presenter
 */
class ChannelPresenter(val IChannel: IChannel): BasePresenter<IChannel>(){

    val channelProvider: ChannelProvider = ChannelProvider()

    fun loadChannel(country: String){
        channelProvider.onLoad(country, object : ListLoadableWithParam.OnLoadListener<ChannelInfo>{
            override fun onSuccess(execute: Boolean, list: ArrayList<ChannelInfo>?) {
                IChannel.loadChannel(execute, list)
            }
        })
    }
}