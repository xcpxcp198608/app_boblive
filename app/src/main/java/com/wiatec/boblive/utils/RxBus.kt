package com.wiatec.boblive.utils

import io.reactivex.subjects.PublishSubject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.Flowable
import io.reactivex.BackpressureStrategy
import io.reactivex.subjects.Subject


class RxBus private constructor() {

    private val mSubject: Subject<Any>

    init {
        mSubject = PublishSubject.create<Any>().toSerialized()
    }

    //发送事件（事件由调用者定义）
    fun post(`object`: Any) {
        mSubject.onNext(`object`)
    }

    //根据事件类型获得对应的Flowable
    private fun <T> getObservable(type: Class<T>): Flowable<T> {
        return mSubject.toFlowable(BackpressureStrategy.BUFFER)
                .ofType(type)
    }

    /**
     * 订阅 Flowable
     * @param type event type
     * @param <T> fanxing
     * @return flowable<T> object
    </T></T> */
    fun <T> subscribe(type: Class<T>): Flowable<T> {
        return getObservable(type)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

    }

    companion object {
        @Volatile
        private var instance: RxBus? = null
        val default: RxBus?
            get() {
                if (instance == null) {
                    synchronized(RxBus::class.java) {
                        if (instance == null) {
                            instance = RxBus()
                        }
                    }
                }
                return instance
            }
    }

}
