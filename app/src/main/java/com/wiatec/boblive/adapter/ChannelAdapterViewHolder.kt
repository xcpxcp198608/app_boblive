package com.wiatec.boblive.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.wiatec.boblive.R

/**
 * Created by patrick on 20/06/2017.
 * create time : 10:11 AM
 */

class ChannelAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var ivIcon: ImageView = itemView.findViewById(R.id.ivIcon) as ImageView
    var tvName: TextView = itemView.findViewById(R.id.tvName) as TextView

}
