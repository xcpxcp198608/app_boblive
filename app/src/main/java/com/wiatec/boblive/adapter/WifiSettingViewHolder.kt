package com.wiatec.boblive.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wiatec.boblive.R
import kotlinx.android.synthetic.main.item_wifi_setting.*
import org.jetbrains.anko.find

/**
 * Created by patrick on 31/07/2017.
 * create time : 11:20 AM
 */
class WifiSettingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val imageView: ImageView = itemView.findViewById(R.id.imageView) as ImageView
    val textView: TextView = itemView.findViewById(R.id.textView) as TextView
}