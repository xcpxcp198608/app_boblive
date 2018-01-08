package com.wiatec.boblive.view

import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.pojo.ResultInfo
import com.wiatec.boblive.pojo.UpgradeInfo

/**
 * Created by patrick on 19/06/2017.
 * create time : 3:40 PM
 */
interface IMain: ICommon {
    fun checkUpgrade(execute: Boolean, upgradeInfo: UpgradeInfo?)
    fun activeAuthorization(execute: Boolean, resultInfo: ResultInfo<AuthorizationInfo>?)
    fun validateAuthorization(execute: Boolean, resultInfo: ResultInfo<AuthorizationInfo>?)
}