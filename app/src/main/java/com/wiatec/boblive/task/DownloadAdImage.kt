package com.wiatec.boblive.task

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.Application
import com.wiatec.boblive.Constant
import com.wiatec.boblive.URL_AD_IMAGE
import com.wiatec.boblive.pojo.ImageInfo
import com.wiatec.boblive.utils.NetUtil
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import com.wiatec.boblive.utils.SysUtil
import java.io.File

/**
 * Created by patrick on 19/07/2017.
 * create time : 10:00 AM
 */
class DownloadAdImage: Runnable {

    override fun run() {
        do{
            if(NetUtil.isConnected) {
                load()
            }
        }while (!NetUtil.isConnected)
    }

    fun load(){
        OkMaster.get(URL_AD_IMAGE)
                .enqueue(object: StringListener(){
                    override fun onSuccess(s: String?) {
                        if(s != null){
                            val imageList: ArrayList<ImageInfo> = Gson().fromJson(s,
                                    object: TypeToken<ArrayList<ImageInfo>>(){}.type)
                            if(imageList.size > 0){
                                val fileNameList: ArrayList<String> = ArrayList()
                                for((_, name, url) in imageList){
                                    OkMaster.download(Application.context)
                                            .name(name)
                                            .url(url)
                                            .path(Constant.image_path())
                                            .startDownload(null)
                                    fileNameList.add(name)
                                }
                                delete(fileNameList)
                            }
                        }
                    }

                    override fun onFailure(e: String?) {
                        if(e != null) Logger.d(e)
                    }
                })
    }

    fun delete(fileNameList: ArrayList<String>){
        val file: File = File(Constant.image_path())
        if(file.exists()){
            val files: Array<File> = file.listFiles()
            if(files.isNotEmpty()){
                files.filterNot { fileNameList.contains(it.name) }
                        .forEach { it.delete() }
            }
        }
    }
}