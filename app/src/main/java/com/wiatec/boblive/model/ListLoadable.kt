package com.wiatec.boblive.model

/**
 * onLoad
 */
interface ListLoadable<T> {

    fun onLoad(onLoadListener: OnLoadListener<T>)
    interface OnLoadListener<T>{
        fun onSuccess(execute: Boolean, list: ArrayList<T>?)
    }
}