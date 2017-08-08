package com.wiatec.boblive.model

import com.wiatec.boblive.instance.Constant
import java.io.File
import java.util.*

/**
 * Created by patrick on 19/07/2017.
 * create time : 9:31 AM
 */
class AdImageProvider: Loadable<String>{

    override fun onLoad(onLoadListener: Loadable.OnLoadListener<String>) {
        val file: File = File(Constant.adimage_path())
        val files: Array<File> = file.listFiles();
        if(files.isNotEmpty()){
            val i = Random().nextInt(files.size)
            val file1 = files[i]
            onLoadListener.onSuccess(true, file1.absolutePath)
        }
    }
}