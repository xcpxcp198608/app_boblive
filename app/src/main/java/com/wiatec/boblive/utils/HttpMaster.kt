package com.px.kotlin.utils

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

/**
 * async http request
 */
class HttpMaster(val url:String){

    interface OnLoadListener {
        fun onSuccess(s: String)
        fun onFailure(e: String)
    }

    fun execute(onLoadListener: OnLoadListener) {
        doAsync {
            val result = URL(url).readText()
            uiThread { onLoadListener.onSuccess(result) }
        }
    }
}