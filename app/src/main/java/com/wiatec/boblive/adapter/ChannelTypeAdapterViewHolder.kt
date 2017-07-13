package com.wiatec.boblive.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wiatec.boblive.R

/**
 * Created by patrick on 20/06/2017.
 * create time : 10:29 AM
 */
class ChannelTypeAdapterViewHolder(view: View): RecyclerView.ViewHolder(view){
    val ivIcon: ImageView = view.findViewById(R.id.ivIcon) as ImageView
    val tvName: TextView = view.findViewById(R.id.tvName) as TextView
}