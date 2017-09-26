package com.wiatec.boblive.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.task.PlayTokenTask
import com.wiatec.boblive.utils.NetUtil

/**
 * Created by patrick on 26/09/2017.
 * create time : 5:44 PM
 */
class NetworkStatusChangeReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent == null) return
        if("android.net.conn.CONNECTIVITY_CHANGE" == intent.action){
            if(NetUtil.isConnected){
                Thread(PlayTokenTask()).start()
            }
        }
    }
}