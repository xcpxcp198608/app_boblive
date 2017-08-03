package com.wiatec.boblive.view

import com.wiatec.boblive.pojo.ChannelInfo

/**
 * Created by patrick on 13/07/2017.
 * create time : 10:20 AM
 */
interface IChannel: ICommon {
    fun loadChannel(execute: Boolean, channelList: ArrayList<ChannelInfo>?)
}