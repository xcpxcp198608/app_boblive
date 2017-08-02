package com.wiatec.boblive.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.wiatec.boblive.R

/**
 * Created by patrick on 31/07/2017.
 * create time : 9:59 AM
 */
class AppViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var imageView: ImageView = itemView.findViewById(R.id.imageView) as ImageView
    var textView: TextView = itemView.findViewById(R.id.textView) as TextView

}