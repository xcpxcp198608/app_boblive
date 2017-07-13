package com.wiatec.boblive.view

import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.pojo.ChannelTypeInfo

/**
 * Created by patrick on 19/06/2017.
 * create time : 3:40 PM
 */
interface IMainActivity {
    fun loadChannelType(channelTypeInfoList: ArrayList<ChannelTypeInfo>)
    fun loadChannel(channelInfoList: ArrayList<ChannelInfo>)
}