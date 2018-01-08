package com.wiatec.boblive.pojo

class VoucherUserInfo {

    var id: Int = 0
    var mac: String? = null
    var category: String? = null
    var voucherId: String? = null
    var level: Int = 0
    var month: Int = 0
    var activateTime: String? = null
    var expiresTime: String? = null
    var createTime: String? = null

    override fun toString(): String {
        return "VoucherUserInfo{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", category='" + category + '\'' +
                ", voucherId='" + voucherId + '\'' +
                ", level=" + level +
                ", month=" + month +
                ", activateTime='" + activateTime + '\'' +
                ", expiresTime='" + expiresTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}'
    }
}
