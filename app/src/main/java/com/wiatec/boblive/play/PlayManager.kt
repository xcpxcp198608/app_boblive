package com.wiatec.boblive.play

import com.wiatec.boblive.TYPE_LIVE
import com.wiatec.boblive.pojo.ChannelInfo

/**
 * play manager
 */
class PlayManager(var channelInfoList: ArrayList<ChannelInfo>, var position: Int) {

    var channelInfo: ChannelInfo? = null

    fun dispatchPlay(){
        channelInfo = channelInfoList[position]
        if(channelInfo == null) return
        if(channelInfo!!.type == TYPE_LIVE){

        }
    }

    fun perviousChannel(){
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