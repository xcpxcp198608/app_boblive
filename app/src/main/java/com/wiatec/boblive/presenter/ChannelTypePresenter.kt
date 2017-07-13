package com.wiatec.boblive.presenter

import com.wiatec.boblive.model.ChannelTypeProvider
import com.wiatec.boblive.model.ListLoadable
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.view.ChannelType

/**
 * Created by patrick on 13/07/2017.
 * create time : 10:51 AM
 */
class ChannelTypePresenter(val channelType: ChannelType) : BasePresenter<ChannelType>() {

    val channelTypeProvider: ChannelTypeProvider = ChannelTypeProvider()

    fun loadChannelType(){
        channelTypeProvider.onLoad(object : ListLoadable.OnLoadListener<ChannelTypeInfo>{
            override fun onSuccess(execute: Boolean, list: ArrayList<ChannelTypeInfo>?) {
                channelType.loadChannelType(execute, list)
            }
        })
    }
}