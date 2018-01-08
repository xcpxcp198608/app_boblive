package com.wiatec.boblive.task

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.instance.*
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.pojo.CODE_OK
import com.wiatec.boblive.pojo.ResultInfo
import com.wiatec.boblive.pojo.VoucherUserInfo
import com.wiatec.boblive.rxevent.ValidateEvent
import com.wiatec.boblive.utils.NetUtil
import com.wiatec.boblive.utils.OkHttp.OkMaster
import com.wiatec.boblive.utils.RxBus
import com.wiatec.boblive.utils.SysUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * check user login status
 */
class ValidateAuth : Runnable {

    override fun run() {
        while (true) {
            Thread.sleep(10000)
            val isVoucher = SPUtil.get(KEY_IS_VOUCHER, false) as Boolean
            if(isVoucher){
                voucherCheck()
            }else {
                check()
            }
        }
    }

    private fun check(){
        if(!NetUtil.isConnected) return
        val authorization: String = SPUtil.get(Application.context!!, KEY_AUTHORIZATION, "").toString()
        if (TextUtils.isEmpty(authorization)) return
        OkMaster.post(URL_VALIDATE)
                .parames(KEY_KEY, authorization)
                .parames(KEY_MAC, SysUtil.getEthernetMac())
                .enqueue(object: Callback{
                    override fun onFailure(call: Call?, e: IOException?) {
                        if(e != null){
                            Logger.d(e)
                        }
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        if (response == null) return
                        try {
                            val s: String = response.body().string()
                            val resultInfo: ResultInfo<AuthorizationInfo> = Gson().fromJson(s, object : TypeToken<ResultInfo<AuthorizationInfo>>() {}.type) ?: return
                            Logger.d(resultInfo)
                            if (resultInfo.code == CODE_OK) {
                                val authorizationInfo: AuthorizationInfo = resultInfo.data!!
                                SPUtil.put(KEY_TEMPORARY, authorizationInfo.temporary)
                                SPUtil.put(KEY_LEVEL, authorizationInfo.level.toString())
                                if (!authorizationInfo.effective) {
                                    RxBus.default!!.post(ValidateEvent("deactivate"))
                                }
                            } else{
                                RxBus.default!!.post(ValidateEvent("key not exists"))
                            }
                        }catch (e: Exception){
                            if (e.message != null) Logger.d(e.message!!)
                        }
                    }
                })
    }

    private fun voucherCheck(){
        if(!NetUtil.isConnected) return
        val authorization: String = SPUtil.get(Application.context!!, KEY_AUTHORIZATION, "").toString()
        if (TextUtils.isEmpty(authorization)) return
        OkMaster.get(URL_VOUCHER_VALIDATE + SysUtil.getEthernetMac())
                .enqueue(object: Callback{
                    override fun onFailure(call: Call?, e: IOException?) {
                        if(e != null){
                            Logger.d(e)
                        }
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        if (response == null) return
                        try {
                            val s: String = response.body().string()
                            val resultInfo: com.wiatec.boblive.pojo.ResultInfo<VoucherUserInfo> =
                                    Gson().fromJson(s, object : TypeToken< com.wiatec.boblive.pojo.ResultInfo<VoucherUserInfo>>() {}.type) ?: return
                            Logger.d(resultInfo)
                            if (resultInfo.code == 200) {
                                val voucherUserInfo: VoucherUserInfo = resultInfo.data!!
                                SPUtil.put(KEY_LEVEL, voucherUserInfo.level.toString())
                                val expiresTime = voucherUserInfo.expiresTime!!
                                val remainTime = ""

                            }else{
                                RxBus.default!!.post(ValidateEvent("validate fail"))
                            }
                        }catch (e: Exception){
                            if (e.message != null) Logger.d(e.message!!)
                        }
                    }
                })
    }
}