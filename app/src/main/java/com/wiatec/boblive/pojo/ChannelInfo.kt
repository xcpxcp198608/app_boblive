package com.wiatec.boblive.pojo

import java.io.Serializable

/**
 * Created by patrick on 20/06/2017.
 * create time : 2:32 PM
 */

class ChannelInfo : Serializable {

    var id: Int = 0
    var channelId: Int = 0
    var skSequence: Int = 0
    var czSequence: Int = 0
    var tag: String? = null
    var name: String? = null
    var url: String? = null
    var icon: String? = null
    var country: String? = null
    var type: Int = 0
    var style: Int = 0
    var visible: Boolean = false
    var backupStart: Boolean = false
    var locked: Boolean = false

    override fun toString(): String {
        return "ChannelInfo{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", skSequence=" + skSequence +
                ", czSequence=" + czSequence +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", style='" + style + '\'' +
                ", visible=" + visible +
                ", backupStart=" + backupStart +
                ", locked=" + locked +
                '}'
    }
}
