package com.wiatec.boblive.helper

import com.wiatec.boblive.instance.TYPE_LIVE
import com.wiatec.boblive.pojo.ChannelInfo

/**
 * play manager
 */
class PlayHelper(var channelInfoList: ArrayList<ChannelInfo>, var position: Int) {

    var channelInfo: ChannelInfo? = null

    fun dispatchPlay(){
        channelInfo = channelInfoList[position]
        if(channelInfo == null) return
        if(channelInfo!!.type == TYPE_LIVE){

        }
    }

    fun previousChannel(){
        position --
        if (position < 0) position = channelInfoList.size - 1
        dispatchPlay()
    }

    fun nextChannel(){
        position ++
        if(position >= channelInfoList.size) position = 0
        dispatchPlay()
    }

}