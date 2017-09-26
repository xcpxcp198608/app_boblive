package com.wiatec.boblive.manager

import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.instance.*
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.utils.AESUtil
import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster

/**
 * Created by patrick on 08/08/2017.
 * create time : 1:50 PM
 */
class PlayManager (private var channelInfoList:List<ChannelInfo>, private var currentPosition: Int){

    var channelInfo: ChannelInfo? = null
    var playListener: PlayListener? = null

    init {
        channelInfo = channelInfoList[currentPosition]
    }

    fun dispatchChannel(){
        val type: Int = channelInfo!!.type
        val url: String = AESUtil.decrypt(channelInfo!!.url, AESUtil.KEY)
        val level: Int = Integer.parseInt(SPUtil.get(Application.context!!, KEY_LEVEL, "1") as String)
        val temporary: Boolean = SPUtil.get(Application.context!!, KEY_TEMPORARY, false) as Boolean
        if(temporary){
            playChannel(type, url)
            return
        }
        when(channelInfo!!.country!!.toUpperCase()){
            TYPE_BASIC -> {
                playChannel(type, url)
            }
            TYPE_PREMIUM -> {
                if(level == 2 || level == 5 || level == 6 || level == 8){
                    playChannel(type, url)
                } else{
                    if (playListener != null) {
                        playListener!!.jumpToAd()
                    }
                }
            }
            TYPE_ADULT ->{
                if(level == 3 || level == 5 || level == 7 || level == 8){
                    playChannel(type, url)
                } else{
                    if (playListener != null) {
                        playListener!!.jumpToAd()
                    }
                }
            }
            TYPE_FILMY ->{
                if(level == 4 || level == 6 || level == 7 || level == 8){
                    playChannel(type, url)
                } else{
                    if (playListener != null) {
                        playListener!!.jumpToAd()
                    }
                }
            }
        }
    }

    private fun playChannel(type: Int, url: String){
        if(playListener == null) return
        if(type == 1){
            playListener!!.play(url)
        }else if(type == 2){
            AppUtil.launchApp(Application.context!!, url)
        }else if(type == 3) {
            relayUrl(url)
        }else{
            Logger.d("channel type error")
        }
    }

    private fun relayUrl(url: String){
        val url1 = if(url.contains("#")){
            url.split("#")[0]
        }else{
            url
        }
        OkMaster.get(url1)
                .enqueue(object: StringListener(){
                    override fun onSuccess(s: String?) {
                        if(s != null && playListener != null){
                            if(url.contains("#")){
                                val uArray = url.split("#")
                                var u = ""
                                for(i in uArray){
                                    u = s + "#" + i
                                }
                                playListener!!.play(u)
                            }else {
                                playListener!!.play(s)
                            }
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