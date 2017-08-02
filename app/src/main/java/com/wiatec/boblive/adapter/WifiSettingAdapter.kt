package com.wiatec.boblive.adapter

import android.net.wifi.ScanResult
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.wiatec.boblive.R
import com.wiatec.boblive.utils.Zoom

/**
 * Created by patrick on 31/07/2017.
 * create time : 11:19 AM
 */
class WifiSettingAdapter(val wifiList: List<ScanResult>):
        RecyclerView.Adapter<WifiSettingViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var onItemFocusListener: OnItemFocusListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): WifiSettingViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_wifi_setting,
                parent, false)
        return WifiSettingViewHolder(view)
    }

    override fun onBindViewHolder(holder: WifiSettingViewHolder?, position: Int) {
        val scanResult: ScanResult = wifiList[position]
        holder!!.textView.text = scanResult.SSID
        setImage(scanResult, holder.imageView)
        holder.itemView.setOnClickListener { v ->
            if(onItemClickListener != null){
                onItemClickListener!!.onItemClick(v, position)
            }
        }
        holder.itemView.setOnFocusChangeListener { v, hasFocus ->
            if(onItemFocusListener != null){
                onItemFocusListener!!.onFocus(v, position, hasFocus)
            }
            if(hasFocus){
                Zoom.zoomIn10to11(v)
            }else{
                Zoom.zoomIn11to10(v)
            }
        }
    }

    override fun getItemCount(): Int {
        return wifiList.size
    }

    fun setImage(scanResult: ScanResult, imageView: ImageView){
        if(isLock(scanResult)){
            when(scanResult.level){
                in -50..0 -> imageView.setImageResource(R.drawable.ic_signal_wifi_4_bar_lock_grey_500_48dp)
                in -70..-50 -> imageView.setImageResource(R.drawable.ic_signal_wifi_3_bar_lock_grey_500_48dp)
                in -80..-70 -> imageView.setImageResource(R.drawable.ic_signal_wifi_2_bar_lock_grey_500_48dp)
                else -> imageView.setImageResource(R.drawable.ic_signal_wifi_1_bar_lock_grey_500_48dp)
            }
        }else{
            when(scanResult.level){
                in -50..0 -> imageView.setImageResource(R.drawable.ic_signal_wifi_4_bar_grey_500_48dp)
                in -70..-50 -> imageView.setImageResource(R.drawable.ic_signal_wifi_3_bar_grey_500_48dp)
                in -80..-70 -> imageView.setImageResource(R.drawable.ic_signal_wifi_2_bar_grey_500_48dp)
                else -> imageView.setImageResource(R.drawable.ic_signal_wifi_1_bar_grey_500_48dp)
            }
        }
    }

    fun isLock(scanResult: ScanResult): Boolean {
        val capabilities = scanResult.capabilities
        if(capabilities.contains("WPA") || capabilities.contains("wpa") ||
                capabilities.contains("WEP") || capabilities.contains("wep")){
            return true
        }
        return false
    }

    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemFocusListener{
        fun onFocus(view: View, position: Int, hasFocus: Boolean)
    }

    fun setOnItemFocusListener(onItemFocusListener: OnItemFocusListener){
        this.onItemFocusListener = onItemFocusListener
    }
}