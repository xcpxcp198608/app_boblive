package com.wiatec.boblive.view

import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.model.AdImageProvider
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.pojo.ImageInfo
import com.wiatec.boblive.pojo.UpgradeInfo

/**
 * Created by patrick on 13/07/2017.
 * create time : 11:11 AM
 */
interface ICommon {
    fun loadAdImage(execute: Boolean, imagePath: String?)
}