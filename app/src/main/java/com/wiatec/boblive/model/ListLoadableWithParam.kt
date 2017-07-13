package com.wiatec.boblive.model

/**
 * load
 */
interface ListLoadableWithParam<T>{
    fun onLoad(param: String, onLoadListener: OnLoadListener<T>)
    interface OnLoadListener<T>{
        fun onSuccess(execute: Boolean, list: ArrayList<T>?)
    }
}