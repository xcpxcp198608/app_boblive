package com.wiatec.boblive.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.Constant
import com.wiatec.boblive.URL_AD_IMAGE
import com.wiatec.boblive.pojo.ImageInfo
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import java.io.File
import java.util.*

/**
 * Created by patrick on 19/07/2017.
 * create time : 9:31 AM
 */
class AdImageProvider: Loadable<String>{

    override fun onLoad(onLoadListener: Loadable.OnLoadListener<String>) {
        val file: File = File(Constant.image_path())
        val files: Array<File> = file.listFiles();
        if(files.isNotEmpty()){
            val i = Random().nextInt(files.size)
            val file1 = files[i]
            onLoadListener.onSuccess(true, file1.absolutePath)
        }
    }
}