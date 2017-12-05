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

    fun getCategory(){
        voucherProvider.getCategory(object : Loadable.OnLoadListener<ResultInfo<VoucherUserCategoryInfo>>{
            override fun onSuccess(execute: Boolean, t: ResultInfo<VoucherUserCategoryInfo>?) {
                iVoucher.onCategory(execute, t)
            }
        })
    }

    fun activate(voucherId: String, category: String, month: String){
        voucherProvider.activate(voucherId, category, month,
                object : LoadableWithParams.OnLoadListener<ResultInfo<VoucherUserInfo>>{
                    override fun onSuccess(execute: Boolean, t: ResultInfo<VoucherUserInfo>?) {
                        iVoucher.onActivate(execute, t)
                    }
                })
    }

}