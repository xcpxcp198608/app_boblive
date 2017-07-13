package com.wiatec.boblive.view

import com.wiatec.boblive.pojo.UpgradeInfo

/**
 * Created by patrick on 17/06/2017.
 * create time : 10:23 AM
 */
interface ISplashActivity {
    fun checkUpgrade(update:Boolean, upgradeInfo: UpgradeInfo)
}