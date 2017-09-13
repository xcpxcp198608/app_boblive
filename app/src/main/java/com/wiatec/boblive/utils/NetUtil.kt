package com.wiatec.boblive.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager

import com.wiatec.boblive.instance.Application
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


/**
 * network utils
 */

object NetUtil {

    /**
     * 判断当前是否有网络连接
     * 需要权限<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"></uses-permission>
     * @return 是否连接
     */
    val isConnected: Boolean
        get() {
            val connectivityManager = Application.context!!
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            if (networkInfo != null) {
                return networkInfo.isAvailable
            } else {
                return false
            }
        }

    /**
     * 判断当前网络连接方式
     * @return 连接类型 ， 0-没有连接 ，1-wifi连接 ，2-移动网络连接 ， 3-有线网络连接
     */
    fun networkConnectType(): Int {
        val connectivityManager = Application.context!!
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state
        //NetworkInfo.State mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        val ethernet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET).state
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            return 1//wifi网络连接
        } else if (ethernet == NetworkInfo.State.CONNECTED || ethernet == NetworkInfo.State.CONNECTING) {
            return 3//有线网络连接
        } else {
            return 0//没有网络连接
        }//else if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING){
        //    return 2;//移动网络连接
        //}
    }

    /**
     * 获取当前系统wifi强度
     * 需要 <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     * @return 信号等级 0-没信号 ，1-信号很差 ，2-信号差 ， 3-信号好 ， 4-信号最好
     */
    //信号最好
    //信号好
    //信号差
    //信号很差
    //没信号
    val wifiLevel: Int
        get() {
            val wifiManager = Application.context!!
                    .getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val level = wifiInfo.rssi
            if (level <= 0 && level >= -50) {
                return 4
            } else if (level < -50 && level >= -70) {
                return 3
            } else if (level < -70 && level >= -80) {
                return 2
            } else if (level < -80 && level >= -100) {
                return 1
            } else {
                return 0
            }
        }

}
