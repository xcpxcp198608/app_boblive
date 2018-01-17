package com.wiatec.boblive.pojo

class VoucherUserInfo {

    var id: Int = 0
    var mac: String? = null
    var voucherId: String? = null
    var level: Int = 0
    var days: Int = 0
    var price: Float = 0F
    var activateTime: String? = null
    var expiresTime: String? = null
    var createTime: String? = null

    override fun toString(): String {
        return "VoucherUserInfo{" +
                "id=" + id +
                ", mac='" + mac + '\'' +
                ", voucherId='" + voucherId + '\'' +
                ", level=" + level +
                ", month=" + days +
                ", price=" + price +
                ", activateTime='" + activateTime + '\'' +
                ", expiresTime='" + expiresTime + '\'' +
                ", createTime='" + createTime + '\'' +
                '}'
    }
}
