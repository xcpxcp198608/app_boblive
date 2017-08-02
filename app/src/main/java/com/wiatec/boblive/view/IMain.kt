package com.wiatec.boblive.view

import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.pojo.UpgradeInfo

/**
 * Created by patrick on 19/06/2017.
 * create time : 3:40 PM
 */
interface IMain {
    fun checkUpgrade(execute: Boolean, upgradeInfo: UpgradeInfo?)
    fun activeAuthorization(execute: Boolean, resultInfo: ResultInfo<AuthorizationInfo>?)
    fun validateAuthorization(execute: Boolean, resultInfo: ResultInfo<AuthorizationInfo>?)
    fun loadAdImage(execute: Boolean, imagePath: String?)
}