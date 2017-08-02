package com.wiatec.boblive.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import com.wiatec.boblive.R
import com.wiatec.boblive.adapter.WifiSettingAdapter
import com.wiatec.boblive.helper.WifiHelper
import kotlinx.android.synthetic.main.activity_wifi_setting.*

/**
 * Created by patrick on 31/07/2017.
 * create time : 11:12 AM
 */
class WifiSettingActivity: AppCompatActivity(){

    var wifiManager: WifiManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_setting)
        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager!!.isWifiEnabled = true
    }

    override fun onStart() {
        super.onStart()
        showWifiList()
    }

    fun showWifiList(){
        val scanResults = wifiManager!!.scanResults
        val wifiSettingAdapter = WifiSettingAdapter(scanResults)
        rcvWifi.adapter = wifiSettingAdapter
        rcvWifi.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        wifiSettingAdapter.setOnItemClickListener(object : WifiSettingAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                showTypeInPasswordDialog(scanResults[position])
            }
        })
    }

    private fun showTypeInPasswordDialog(scanResult: ScanResult) {
        val dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()
        val window: Window = dialog.window
        window.setContentView(R.layout.dialog_wifi_password)
        val etAuthorization: EditText = window.findViewById(R.id.etWifiPassword) as EditText
        val btConfirm: Button = window.findViewById(R.id.btConfirm) as Button
        btConfirm.setOnClickListener {
            val password = etAuthorization.text.toString()
            if(!TextUtils.isEmpty(password) && password.length >= 8){
                val encryptType = WifiHelper.getEncryptType(scanResult)
                val wifiConfiguration = WifiHelper.makeWifiConfiguration(encryptType,
                        scanResult.SSID, password)
                val netId = WifiHelper.addWifiConfiguration(wifiManager!!, wifiConfiguration)
                WifiHelper.connectWifi(wifiManager!!, netId)
                dialog.dismiss()
                startActivity(Intent(this@WifiSettingActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}