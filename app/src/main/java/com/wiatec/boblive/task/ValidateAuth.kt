package com.wiatec.boblive.task

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.Application
import com.wiatec.boblive.URL_VALIDATE
import com.wiatec.boblive.entity.CODE_OK
import com.wiatec.boblive.entity.CODE_UNAUTHORIZED
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.rxevent.ValidateEvent
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
            check()
        }
    }

    private fun check(){
        val authorization: String = SPUtil.get(Application.context!!, "authorization", "").toString()
        if (TextUtils.isEmpty(authorization)) return
        OkMaster.post(URL_VALIDATE)
                .parames("key", authorization)
                .parames("mac", SysUtil.getWifiMac())
                .enqueue(object: Callback{
                    override fun onFailure(call: Call?, e: IOException?) {
                        if(e != null){
                            Logger.d(e)
                        }
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        if(response == null) return
                        val s:String = response.body().string()
                        val resultInfo: ResultInfo<AuthorizationInfo> = Gson().fromJson(s, object :TypeToken<ResultInfo<AuthorizationInfo>>(){}.type) ?: return
                        Logger.d(resultInfo)
                        if(resultInfo.code == CODE_OK){
                            val authorizationInfo: AuthorizationInfo = resultInfo.data[0]
                            SPUtil.put(Application.context!!, "experience", resultInfo.message)
                            SPUtil.put(Application.context!!, "level", authorizationInfo.level.toString())
                            if(authorizationInfo.level.toInt() == 0 ){
                                RxBus.default!!.post(ValidateEvent("level limit"))
                            }
                        }else if(resultInfo.code == CODE_UNAUTHORIZED){
                            RxBus.default!!.post(ValidateEvent("key not exists"))
                        }
                    }
                })

    }
}