package com.wiatec.boblive.presenter

import com.wiatec.boblive.instance.Application
import com.wiatec.boblive.instance.URL_ACTIVE
import com.wiatec.boblive.model.*
import com.wiatec.boblive.pojo.*
import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.view.IVoucher

/**
 * Created by patrick on 19/06/2017.
 * create time : 3:40 PM
 */
class VoucherPresenter(val iVoucher: IVoucher): BasePresenter<IVoucher>() {

    private val voucherProvider: VoucherProvider = VoucherProvider()
    private val authorizationProvider: AuthorizationProvider = AuthorizationProvider()

    fun activate(voucherId: String, days: String, price: String){
        voucherProvider.activate(voucherId, days, price,
                object : LoadableWithParams.OnLoadListener<ResultInfo<VoucherUserInfo>>{
                    override fun onSuccess(execute: Boolean, t: ResultInfo<VoucherUserInfo>?) {
                        iVoucher.onActivate(execute, t)
                    }
                })
    }

    fun activeAuthorization(authorization: String){
        authorizationProvider.onLoad(URL_ACTIVE, authorization,
                object : LoadableWithParams.OnLoadListener<ResultInfo<AuthorizationInfo>>{
                    override fun onSuccess(execute: Boolean, t: ResultInfo<AuthorizationInfo>?) {
                        iVoucher.activeAuthorization(execute, t)
                    }
                })
    }

}