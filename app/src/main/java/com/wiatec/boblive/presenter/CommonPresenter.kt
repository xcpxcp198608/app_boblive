package com.wiatec.boblive.presenter

import com.wiatec.boblive.model.AdImageProvider
import com.wiatec.boblive.model.Loadable
import com.wiatec.boblive.view.ICommon

/**
 * Created by patrick on 31/07/2017.
 * create time : 9:41 AM
 */
class CommonPresenter(ICommon: ICommon): BasePresenter<ICommon>(){

    val adImageProvider: AdImageProvider = AdImageProvider()

    fun loadAdImage(){
        adImageProvider.onLoad(object: Loadable.OnLoadListener<String>{
            override fun onSuccess(execute: Boolean, t: String?) {

            }
        })
    }
}