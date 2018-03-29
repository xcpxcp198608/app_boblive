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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


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
                            val resultInfo: ResultInfo<VoucherUserInfo> =
                                    Gson().fromJson(s, object : TypeToken<ResultInfo<VoucherUserInfo>>() {}.type) ?: return
                            Logger.d(resultInfo)
                            if (resultInfo.code == 200) {
                                val voucherUserInfo: VoucherUserInfo = resultInfo.data!!
                                SPUtil.put(KEY_LEVEL, voucherUserInfo.level.toString())
                                val expiresTime = voucherUserInfo.expiresTime!!
                                val unixTime = getUnixFromStr(expiresTime)
                                if(unixTime <= System.currentTimeMillis()){
                                    SPUtil.put(KEY_AUTHORIZATION, "")
                                }
                                SPUtil.put(KEY_VOUCHER_EXPIRES_TIME, expiresTime)
                                var leftMillsSeconds = System.currentTimeMillis() - getUnixFromStr(expiresTime)
                                if(leftMillsSeconds < 0){leftMillsSeconds = 0}
                                SPUtil.put(KEY_VOUCHER_LEFT_MILLS_SECOND, leftMillsSeconds)

                                val level = voucherUserInfo.level
                                if(level <= 0){
                                    RxBus.default!!.post(ValidateEvent("level 0"))
                                }
                            }else{
                                RxBus.default!!.post(ValidateEvent("validate fail"))
                            }
                        }catch (e: Exception){
                            if (e.message != null) Logger.d(e.message!!)
                        }
                    }
                })
    }

    private fun getUnixFromStr(time: String): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale("en"))
        val date: Date
        try {
            date = simpleDateFormat.parse(time)
        } catch (e: ParseException) {
            return 0
        }
        return date.time
    }


}