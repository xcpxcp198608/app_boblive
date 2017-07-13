package com.wiatec.boblive.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.wiatec.boblive.R
import com.wiatec.boblive.pojo.ChannelTypeInfo

class ChannelTypeAdapter(val channelTypeInfoList: ArrayList<ChannelTypeInfo>,
                         val onItemSelectListener: OnItemSelectListener): RecyclerView.Adapter<ChannelTypeAdapterViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChannelTypeAdapterViewHolder {
        context = parent!!.context
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_channel_type, parent, false)
        return ChannelTypeAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChannelTypeAdapterViewHolder?, position: Int) {
        val channelTypeInfo = channelTypeInfoList[position]
        if (holder == null) return
        holder.tvName.text = channelTypeInfo.name
        Glide.with(context)
                .load(channelTypeInfo.icon)
                .placeholder(R.drawable.logo_live)
                .error(R.drawable.logo_live)
                .dontAnimate()
                .into(holder.ivIcon)
        holder.itemView.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                onItemSelectListener.onItemSelected(v, position, hasFocus)
            }
        }
    }

    override fun getItemCount(): Int {
        return channelTypeInfoList.size
    }

    interface OnItemSelectListener{
        fun onItemSelected(view: View, position: Int, hasFocus: Boolean)
    }

}