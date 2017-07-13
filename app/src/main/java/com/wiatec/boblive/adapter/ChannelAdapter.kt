package com.wiatec.boblive.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.wiatec.boblive.R
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.utils.Zoom

/**
 * Created by patrick on 19/06/2017.
 * create time : 9:54 AM
 */
class ChannelAdapter(val channelInfoList: ArrayList<ChannelInfo>,
                     val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<ChannelAdapterViewHolder>() {

    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChannelAdapterViewHolder {
        context = parent!!.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_channel, parent, false)
        return ChannelAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelAdapterViewHolder?, position: Int) {
        val channelInfo = channelInfoList[position]
        if(holder == null) return
        holder.tvName.text = channelInfo.name
        Glide.with(context)
                .load(channelInfo.url)
                .placeholder(R.drawable.logo_live)
                .error(R.drawable.logo_live)
                .dontAnimate()
                .into(holder.ivIcon)
        holder.itemView.setOnClickListener { v -> onItemClickListener.onItemClick(v, position)}
        holder.itemView.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                holder.tvName.isSelected = true
                Zoom.zoomIn10to11(v)
            }else{
                holder.tvName.isSelected = false
                Zoom.zoomIn11to10(v)
            }
        }
    }

    override fun getItemCount(): Int {
        return channelInfoList.size
    }

    interface OnItemClickListener{
        fun onItemClick(view: View, position: Int)
    }
}