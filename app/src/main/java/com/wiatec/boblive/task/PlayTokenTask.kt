package com.wiatec.boblive.task

import android.text.TextUtils
import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.instance.Application
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import com.wiatec.boblive.utils.AESUtil
import com.wiatec.boblive.utils.NetUtil
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Created by patrick on 18/09/2017.
 * create time : 6:12 PM
 */
private val URL = "http://apieu.protv.company/v1/get_token.do?"
private val PRE = "B0015C41E7"
private val PWD = "Dfuk5ygo8AelWvGj"

class PlayTokenTask: TimerTask() {
    override fun run() {
        do{
            start()
        }while (!NetUtil.isConnected)
    }

    private fun start() {
        var time = System.currentTimeMillis()
        time /= 1000
        val t = AESUtil.MD5(PRE + PWD + time)
        val url = URL + "reg_date=" + time + "&token=" + t
        OkMaster.get(url)
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        if(TextUtils.isEmpty(s)) return
//                        Logger.d(s!!)
                        try {
                            val jsonObject = JSONObject(s)
                            val data = jsonObject.getJSONObject("data")
                            val streamToken = data.getString("token")
                            Logger.d(streamToken)
                            SPUtil.put(Application.context!!, "streamToken", streamToken)
                        } catch (e: JSONException) {
                            Logger.d("token json format error")
                        }
                    }

                    override fun onFailure(e: String?) {
                        if(e != null) Logger.d(e)
                    }
                })
    }
}