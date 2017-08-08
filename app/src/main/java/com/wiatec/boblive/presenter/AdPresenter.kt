package com.wiatec.boblive.presenter

import com.wiatec.boblive.model.AdImageProvider
import com.wiatec.boblive.model.Loadable
import com.wiatec.boblive.view.ICommon

/**
 * Created by patrick on 08/08/2017.
 * create time : 2:28 PM
 */
class AdPresenter(val iCommon: ICommon): BasePresenter<ICommon>(){

    val adImageProvider: AdImageProvider = AdImageProvider()

    fun loadAdImage(){
        adImageProvider.onLoad(object : Loadable.OnLoadListener<String>{
            override fun onSuccess(execute: Boolean, t: String?) {
                iCommon.loadAdImage(execute, t)
            }
        })
    }
}