package com.wiatec.boblive.view

import com.wiatec.boblive.pojo.*

/**
 * Created by patrick on 04/12/2017.
 * create time : 10:11 AM
 */
interface IVoucher {

    fun onActivate(execute: Boolean, resultInfo: ResultInfo<VoucherUserInfo>?)
    fun activeAuthorization(execute: Boolean, resultInfo: ResultInfo<AuthorizationInfo>?)
}