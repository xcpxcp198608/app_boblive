package com.wiatec.boblive.pojo

/**
 * update information of this app
 */
data class UpgradeInfo(var id: Int =0,
                       var packageName: String="",
                       var url: String="",
                       var version: String="",
                       var code: Int=0,
                       var info: String="")
