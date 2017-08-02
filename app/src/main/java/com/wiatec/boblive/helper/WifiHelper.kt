package com.wiatec.boblive.helper

import android.net.wifi.ScanResult
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager

/**
 * Created by patrick on 31/07/2017.
 * create time : 2:32 PM
 */
const val ENCRYPT_NONE = 0
const val ENCRYPT_WEP = 1
const val ENCRYPT_PSK = 2
const val ENCRYPT_EAP = 3

object WifiHelper {

    /**
     * 获取wifi加密类型
     */
    fun getEncryptType(scanResult: ScanResult): Int {
        if(scanResult.capabilities.contains("wep") || scanResult.capabilities.contains("WEP")){
            return ENCRYPT_WEP
        }else if(scanResult.capabilities.contains("psk") || scanResult.capabilities.contains("PSK")){
            return ENCRYPT_PSK
        }else if(scanResult.capabilities.contains("eap") || scanResult.capabilities.contains("EAP")){
            return ENCRYPT_EAP
        }else{
            return ENCRYPT_NONE
        }
    }

    fun convertToQuotedString(s: String): String{
        return "\""+s+"\""
    }

    /**
     * 根据加密类型，ssid, 密码得到wifi配置
     */
    fun makeWifiConfiguration(encryptType: Int, ssid: String, password: String): WifiConfiguration {
        val wifiConfiguration: WifiConfiguration = WifiConfiguration()
        wifiConfiguration.SSID = convertToQuotedString(ssid)
        wifiConfiguration.hiddenSSID = true
        when(encryptType){
            ENCRYPT_NONE -> wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
            ENCRYPT_WEP -> {wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
                wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED)
                val length = password.length
                if ((length == 10 || length == 26 || length == 58) && password.matches(Regex("[0-9A-Fa-f]*"))) {
                    wifiConfiguration.wepKeys[0] = password
                } else {
                    wifiConfiguration.wepKeys[0] = '"' + password + '"'
                }
            }
            ENCRYPT_PSK ->{ wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
                if (password.isNotEmpty()) {
                    if (password.matches(Regex("[0-9A-Fa-f]*"))) {
                        wifiConfiguration.preSharedKey = password
                    } else {
                        wifiConfiguration.preSharedKey = '"' + password + '"'
                    }
                }
            }
            ENCRYPT_EAP -> {wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP)
                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X)  }
        }
        return wifiConfiguration
    }

    /**
     * 增加wifi到配置列表， 得到配置id
     */
    fun addWifiConfiguration(wifiManager: WifiManager, wifiConfiguration: WifiConfiguration): Int {
        return wifiManager.addNetwork(wifiConfiguration)
    }

    /**
     * 通过网络id连接wifi
     */
    fun connectWifi(wifiManager: WifiManager, netId: Int){
        wifiManager.enableNetwork(netId, true)
    }
}