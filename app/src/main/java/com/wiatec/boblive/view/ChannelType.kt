package com.wiatec.boblive.view

import com.wiatec.boblive.pojo.ChannelTypeInfo

/**
 * Created by patrick on 13/07/2017.
 * create time : 10:50 AM
 */
interface ChannelType {

    fun loadChannelType(execute: Boolean, channelTypeList: ArrayList<ChannelTypeInfo>?)
}