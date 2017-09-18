package com.wiatec.boblive.task

import android.text.TextUtils
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import com.wiatec.boblive.utils.AESUtil
/**
 * Created by patrick on 18/09/2017.
 * create time : 6:12 PM
 */
private val URL = "http://apius.protv.company/v1/get_token.do?"
private val PRE = "BTVi35C41E7"
private val PWD = "Ho2oMcqUZMMvFzqb"

class PlayTokenTask: Runnable {
    override fun run() {
        start()
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
                        Logger.d(s!!)
                    }

                    override fun onFailure(e: String?) {
                        if(e != null) Logger.d(e)
                    }
                })
    }
}