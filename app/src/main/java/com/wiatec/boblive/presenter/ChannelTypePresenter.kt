package com.wiatec.boblive.presenter

import com.wiatec.boblive.model.AdImageProvider
import com.wiatec.boblive.model.ChannelTypeProvider
import com.wiatec.boblive.model.ListLoadable
import com.wiatec.boblive.model.Loadable
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.pojo.ImageInfo
import com.wiatec.boblive.view.ChannelType

/**
 * Created by patrick on 13/07/2017.
 * create time : 10:51 AM
 */
class ChannelTypePresenter(val channelType: ChannelType) : BasePresenter<ChannelType>() {

    val channelTypeProvider: ChannelTypeProvider = ChannelTypeProvider()
    val adImageProvider: AdImageProvider = AdImageProvider()

    fun loadChannelType(){
        channelTypeProvider.onLoad(object : ListLoadable.OnLoadListener<ChannelTypeInfo>{
            override fun onSuccess(execute: Boolean, list: ArrayList<ChannelTypeInfo>?) {
                channelType.loadChannelType(execute, list)
            }
        })
    }

    fun loadAdImage(){
        adImageProvider.onLoad(object: Loadable.OnLoadListener<String>{
            override fun onSuccess(execute: Boolean, t: String?) {
                channelType.loadAdImage(execute, t)
            }
        })
    }
}