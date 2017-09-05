package com.wiatec.boblive.presenter

import com.wiatec.boblive.model.*
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.view.IChannelType

/**
 * Created by patrick on 13/07/2017.
 * create time : 10:51 AM
 */
class ChannelTypePresenter(val IChannelType: IChannelType) : BasePresenter<IChannelType>() {

    private val channelTypeProvider = ChannelTypeProvider()
    private val adImageProvider = AdImageProvider()

    fun loadChannelType(type: String){
        channelTypeProvider.onLoad(type, object : ListLoadableWithParam.OnLoadListener<ChannelTypeInfo>{
            override fun onSuccess(execute: Boolean, list: ArrayList<ChannelTypeInfo>?) {
                IChannelType.loadChannelType(execute, list)
            }
        })
    }

    fun loadAdImage(){
        adImageProvider.onLoad(object: Loadable.OnLoadListener<String>{
            override fun onSuccess(execute: Boolean, t: String?) {
                IChannelType.loadAdImage(execute, t)
            }
        })
    }
}