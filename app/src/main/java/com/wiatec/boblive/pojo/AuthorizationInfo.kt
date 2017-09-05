package com.wiatec.boblive.pojo

/**
 * Created by xuchengpeng on 23/06/2017.
 */
class AuthorizationInfo {

    var id: Int = 0
    var key: String? = null
    var mac: String? = null
    var active: Short = 0
    var activeDate: String? = null
    var activeTime: Long = 0
    var sales: String? = null
    var dealer: String? = null
    var leader: String? = null
    /**
     * level = 2 PREMIUM
     * level = 3 ADULT
     * level = 4 FILMY
     * level = 5 PREMIUM + ADULT
     * level = 6 PREMIUM + FILMY
     * level = 7 ADULT + FILMY
     * level = 8 PREMIUM + ADULT + FILMY
     */
    var level: Short = 0
    var memberDate: String? = null
    var memberTime: Long = 0
    var effective: Boolean = true
    var temporary: Boolean = true


    override fun toString(): String {
        return "AuthorizationInfo{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", mac='" + mac + '\'' +
                ", active=" + active +
                ", activeDate='" + activeDate + '\'' +
                ", activeTime=" + activeTime +
                ", sales=" + sales +
                ", dealer=" + dealer +
                ", leader=" + leader +
                ", level=" + level +
                ", memberDate='" + memberDate + '\'' +
                ", memberTime=" + memberTime +
                ", effective=" + effective +
                ", temporary=" + temporary +
                '}'
    }
}
