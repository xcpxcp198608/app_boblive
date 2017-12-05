package com.wiatec.boblive.view

import com.wiatec.boblive.pojo.ResultInfo
import com.wiatec.boblive.pojo.UpgradeInfo
import com.wiatec.boblive.pojo.VoucherUserCategoryInfo
import com.wiatec.boblive.pojo.VoucherUserInfo

/**
 * Created by patrick on 04/12/2017.
 * create time : 10:11 AM
 */
interface IVoucher {

    fun onCategory(execute: Boolean, resultInfo: ResultInfo<VoucherUserCategoryInfo>?)
    fun onActivate(execute: Boolean, resultInfo: ResultInfo<VoucherUserInfo>?)
}