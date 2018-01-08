package com.wiatec.boblive.pojo

/**
 * the result that return to user after user's request
 */
const val CODE_OK = 200
class ResultInfo<T> {

    var code: Int = 0
    var message: String? = null
    var data: T? = null
    var dataList: List<T>? = null

    override fun toString(): String {
        return "ResultInfo{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", dataList=" + dataList +
                '}'
    }
}
