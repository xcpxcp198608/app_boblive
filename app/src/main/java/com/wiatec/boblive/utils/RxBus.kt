package com.wiatec.boblive.utils

import rx.Observable
import rx.schedulers.Schedulers
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

class RxBus// RxBus单例
    private constructor() {

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private val rxBus = SerializedSubject(PublishSubject.create<Any>())

    //发送事件
    fun post(any: Any) {
        rxBus.onNext(any)
    }

    /**
     * 根据事件类型进行订阅过滤返回一个事件类型对应的Observable
     * @param event  事件
     * *
     * @param <T> 事件类型
     * *
     * @return 返回事件类型对应的Observable,订阅时直接使用subscribe()
    </T> */
    fun <T> toObservable(event: Class<T>): Observable<T> {
        return rxBus
                .subscribeOn(Schedulers.io())
                .filter { o -> event.isInstance(o) }
                .cast(event)
    }

    companion object {
        @Volatile private var instance: RxBus? = null
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
