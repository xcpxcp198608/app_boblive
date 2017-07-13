package com.wiatec.boblive.pojo

import java.io.Serializable

/**
 * Created by patrick on 20/06/2017.
 * create time : 2:32 PM
 */

class ChannelInfo : Serializable {

    var id: Int = 0
    var channelId: Int = 0
    var sequence: Int = 0
    var tag: String? = null
    var name: String? = null
    var url: String? = null
    var icon: String? = null
    var type: String? = null
    var country: String? = null
    var style: String? = null
    var visible: Int = 0
    var europe: Int = 0

    override fun toString(): String {
        return "ChannelInfo{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", sequence=" + sequence +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", type='" + type + '\'' +
                ", country='" + country + '\'' +
                ", style='" + style + '\'' +
                ", visible=" + visible +
                ", europe=" + europe +
                '}'
    }
}
