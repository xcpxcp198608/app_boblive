package com.wiatec.boblive.pojo

/**
 * Created by xuchengpeng on 23/06/2017.
 */
class AuthorizationInfo {

    var id: Int = 0
    var key: String? = null
    var mac: String? = null
    var group: String? = null
    var active: Short = 0
    var activeDate: String? = null
    var activeTime: Long = 0
    var level: Short = 0
    var memberDate: String? = null
    var memberTime: Long = 0

    override fun toString(): String {
        return "AuthorizationInfo{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", mac='" + mac + '\'' +
                ", group='" + group + '\'' +
                ", active=" + active +
                ", activeDate='" + activeDate + '\'' +
                ", activeTime=" + activeTime +
                ", level=" + level +
                ", memberDate='" + memberDate + '\'' +
                ", memberTime=" + memberTime +
                '}'
    }
}
