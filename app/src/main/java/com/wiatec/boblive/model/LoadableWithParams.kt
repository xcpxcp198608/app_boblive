package com.wiatec.boblive.model

import com.wiatec.boblive.pojo.AuthorizationInfo

/**
 * onLoad
 */
interface LoadableWithParams<T> {

    fun onLoad(url: String, authorization: String, onLoadListener: OnLoadListener<T>)
    interface OnLoadListener<T>{
        fun onSuccess(execute: Boolean, t: T?)
    }
}