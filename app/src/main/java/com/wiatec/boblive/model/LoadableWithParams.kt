package com.wiatec.boblive.model

import com.wiatec.boblive.pojo.AuthorizationInfo

/**
 * onLoad
 */
interface LoadableWithParams<T> {

    fun onLoad(param1: String, param2: String, onLoadListener: OnLoadListener<T>)
    interface OnLoadListener<T>{
        fun onSuccess(execute: Boolean, t: T?)
    }
}