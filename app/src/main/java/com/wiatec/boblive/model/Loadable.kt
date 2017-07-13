package com.wiatec.boblive.model

/**
 * onLoad
 */
interface Loadable<T> {

    fun onLoad(onLoadListener: OnLoadListener<T>)
    interface OnLoadListener<T>{
        fun onSuccess(execute: Boolean, t: T?)
    }
}