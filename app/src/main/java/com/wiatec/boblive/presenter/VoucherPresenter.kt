package com.wiatec.boblive.presenter

import com.wiatec.boblive.instance.Application
import com.wiatec.boblive.model.*
import com.wiatec.boblive.pojo.ResultInfo
import com.wiatec.boblive.pojo.UpgradeInfo
import com.wiatec.boblive.pojo.VoucherUserCategoryInfo
import com.wiatec.boblive.pojo.VoucherUserInfo
import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.view.IVoucher

/**
 * Created by patrick on 19/06/2017.
 * create time : 3:40 PM
 */
class VoucherPresenter(val iVoucher: IVoucher): BasePresenter<IVoucher>() {

    private val voucherProvider: VoucherProvider = VoucherProvider()

    fun activate(voucherId: String, days: String, price: String){
        voucherProvider.activate(voucherId, days, price,
                object : LoadableWithParams.OnLoadListener<ResultInfo<VoucherUserInfo>>{
                    override fun onSuccess(execute: Boolean, t: ResultInfo<VoucherUserInfo>?) {
                        iVoucher.onActivate(execute, t)
                    }
                })
    }

}