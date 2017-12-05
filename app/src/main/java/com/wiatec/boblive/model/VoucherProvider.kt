package com.wiatec.boblive.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.instance.Constant
import com.wiatec.boblive.instance.URL_VOUCHER_ACTIVATE
import com.wiatec.boblive.instance.URL_VOUCHER_CATEGORY
import com.wiatec.boblive.instance.URL_VOUCHER_VALIDATE
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.pojo.ResultInfo
import com.wiatec.boblive.pojo.VoucherUserCategoryInfo
import com.wiatec.boblive.pojo.VoucherUserInfo
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import com.wiatec.boblive.utils.SysUtil
import java.io.File
import java.util.*

/**
 * Created by patrick on 19/07/2017.
 * create time : 9:31 AM
 */
class VoucherProvider{

    fun getCategory(onLoadListener: Loadable.OnLoadListener<ResultInfo<VoucherUserCategoryInfo>>){
        OkMaster.get(URL_VOUCHER_CATEGORY)
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        val resultInfo: ResultInfo<VoucherUserCategoryInfo> = Gson().fromJson(s,
                                object : TypeToken<ResultInfo<VoucherUserCategoryInfo>>(){}.type)
                        onLoadListener.onSuccess(true, resultInfo)
                    }

                    override fun onFailure(e: String?) {
                        Logger.d(e!!)
                        onLoadListener.onSuccess(false, null)
                    }
                })

    }

    fun activate(voucherId: String, category: String, month: String, onLoadListener:
                            LoadableWithParams.OnLoadListener<ResultInfo<VoucherUserInfo>>) {
        OkMaster.post(URL_VOUCHER_ACTIVATE)
                .parames("voucherId", voucherId)
                .parames("category", category)
                .parames("mac", SysUtil.getEthernetMac())
                .parames("month", month)
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        Logger.d(s!!)
                        val resultInfo: ResultInfo<VoucherUserInfo> = Gson().fromJson(s,
                                object : TypeToken<ResultInfo<VoucherUserInfo>>(){}.type)
                        onLoadListener.onSuccess(true, resultInfo)
                    }

                    override fun onFailure(e: String?) {
                        Logger.d(e!!)
                        onLoadListener.onSuccess(false, null)
                    }
                })
    }

}