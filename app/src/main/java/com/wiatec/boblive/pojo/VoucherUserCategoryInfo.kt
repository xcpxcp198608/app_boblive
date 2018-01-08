package com.wiatec.boblive.pojo

class VoucherUserCategoryInfo {

    var id: Int = 0
    var category: String? = null
    var level: Int = 0
    var price: Float = 0.toFloat()

    //开始计算bonus的起始月数
    var startMonth: Int = 0
    var bonus: Int = 0
    var description: String? = null

    override fun toString(): String {
        return "VoucherUserCategoryInfo{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", level=" + level +
                ", price=" + price +
                ", startMonth=" + startMonth +
                ", bonus=" + bonus +
                ", description='" + description + '\'' +
                '}'
    }
}
