package com.wiatec.boblive.manager

import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.instance.*
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.utils.AESUtil
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster

/**
 * Created by patrick on 08/08/2017.
 * create time : 1:50 PM
 */
class PlayManager (private var channelInfoList:List<ChannelInfo>, var currentPosition: Int){

    var channelInfo: ChannelInfo? = null
    var playListener: PlayListener? = null

    init {
        channelInfo = channelInfoList[currentPosition]
    }

    fun dispatchChannel(){
        val type: String = channelInfo!!.type!!
        val url: String = AESUtil.decrypt(channelInfo!!.url, AESUtil.KEY)
        val level: Int = Integer.parseInt(SPUtil.get(Application.context!!, KEY_LEVEL, "1") as String)
        val temporary: Boolean = SPUtil.get(Application.context!!, KEY_TEMPORARY, false) as Boolean
        if(type == KEY_LIVE){
            if(temporary){
                if(playListener != null){
                    playListener!!.play(url)
                }
            }else{
                when (channelInfo!!.country!!.toUpperCase()){
                    TYPE_BASIC -> {
                        if(playListener != null){
                            playListener!!.play(url)
                        }
                    }
                    TYPE_PREMIUM -> {
                        if(level == 2){
                            if(playListener != null){
                                playListener!!.play(url)
                            }
                        } else{
                            if (playListener != null) {
                                playListener!!.jumpToAd()
                            }
                        }
                    }
                    TYPE_ADULT ->{
                        if(level == 3){
                            if(playListener != null){
                                playListener!!.play(url)
                            }
                        } else{
                            if (playListener != null) {
                                playListener!!.jumpToAd()
                            }
                        }
                    }
                    TYPE_FILMY ->{
                        if(level == 4){
                            if(playListener != null){
                                playListener!!.play(url)
                            }
                        } else{
                            if (playListener != null) {
                                playListener!!.jumpToAd()
                            }
                        }
                    }
                }
            }
        }else if (type == KEY_RELAY){
            if(temporary){
                relayUrl(url)
            }else{
                when (channelInfo!!.country){
                    TYPE_BASIC -> {
                        relayUrl(url)
                    }
                    TYPE_PREMIUM -> {
                        if(level == 2 || level == 5 || level == 6 || level == 8){
                            relayUrl(url)
                        } else{
                            if (playListener != null) {
                                playListener!!.jumpToAd()
                            }
                        }
                    }
                    TYPE_ADULT ->{
                        if(level == 3 || level == 5 || level == 7 || level == 8){
                            relayUrl(url)
                        } else{
                            if (playListener != null) {
                                playListener!!.jumpToAd()
                            }
                        }
                    }
                    TYPE_FILMY ->{
                        if(level == 4 || level == 6 || level == 7 || level == 8){
                            relayUrl(url)
                        } else{
                            if (playListener != null) {
                                playListener!!.jumpToAd()
                            }
                        }
                    }
                }
            }
        }else{
            Logger.d("channel type error")
        }
    }

    fun relayUrl(url: String){
        OkMaster.get(url)
                .enqueue(object: StringListener(){
                    override fun onSuccess(s: String?) {
                        if(s != null && playListener != null){
                            playListener!!.play(url)
                        }
                    }

                    override fun onFailure(e: String?) {
                        if(e != null) Logger.d(e)
                    }
                })
    }

    fun previousChannel(){
        currentPosition --
        if(currentPosition < 0 ){
            currentPosition = channelInfoList.size -1
        }
        channelInfo = channelInfoList[currentPosition]
        dispatchChannel()
    }

    fun nextChannel(){
        currentPosition ++
        if(currentPosition >= channelInfoList.size){
            currentPosition = 0
        }
        channelInfo = channelInfoList[currentPosition]
        dispatchChannel()
    }

    interface PlayListener{
        fun play(url: String)
        fun jumpToAd()
    }

}