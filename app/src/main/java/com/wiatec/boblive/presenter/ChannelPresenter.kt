package com.wiatec.boblive.presenter

import com.wiatec.boblive.model.AdImageProvider
import com.wiatec.boblive.model.ChannelProvider
import com.wiatec.boblive.model.ListLoadableWithParam
import com.wiatec.boblive.model.Loadable
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.view.IChannel

/**
 * IChannel presenter
 */
class ChannelPresenter(val iChannel: IChannel): BasePresenter<IChannel>(){

    private val channelProvider = ChannelProvider()
    private val adImageProvider = AdImageProvider()

    fun loadChannel(country: String){
        channelProvider.onLoad(country, object : ListLoadableWithParam.OnLoadListener<ChannelInfo>{
            override fun onSuccess(execute: Boolean, list: ArrayList<ChannelInfo>?) {
                iChannel.loadChannel(execute, list)
            }
        })
    }

    fun loadAdImage(){
        adImageProvider.onLoad(object: Loadable.OnLoadListener<String>{
            override fun onSuccess(execute: Boolean, t: String?) {
                iChannel.loadAdImage(execute, t)
            }
        })
    }
}