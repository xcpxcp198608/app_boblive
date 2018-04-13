package com.wiatec.boblive.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.instance.Application
import com.wiatec.boblive.instance.KEY_LANG
import com.wiatec.boblive.instance.URL_VOUCHER_ACTIVATE
import com.wiatec.boblive.pojo.ResultInfo
import com.wiatec.boblive.pojo.VoucherUserInfo
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import com.wiatec.boblive.utils.SysUtil

/**
 * Created by patrick on 19/07/2017.
 * create time : 9:31 AM
 */
class VoucherProvider{

    fun activate(voucherId: String, days: String, price: String, onLoadListener:
                            LoadableWithParams.OnLoadListener<ResultInfo<VoucherUserInfo>>) {
        OkMaster.post(URL_VOUCHER_ACTIVATE)
                .parames("voucherId", voucherId)
                .parames("days", days)
                .parames("mac", SysUtil.getEthernetMac())
                .parames("price", price)
                .parames(KEY_LANG, SysUtil.getLanguage(Application.context))
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
//                        Logger.d(s!!)
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