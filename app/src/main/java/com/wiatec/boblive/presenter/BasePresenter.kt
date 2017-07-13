package com.wiatec.boblive.presenter

import java.lang.ref.WeakReference

/**
 * Created by patrick on 17/06/2017.
 * create time : 11:09 AM
 */

open class BasePresenter<V> {

    private var weakReference: WeakReference<V>? = null

    fun attach(v: V) {
        weakReference = WeakReference(v)
    }

    fun detach() {
        if (weakReference != null) {
            weakReference!!.clear()
            weakReference = null
        }
    }

    fun getView(): V? {
        return weakReference!!.get()
    }
}
