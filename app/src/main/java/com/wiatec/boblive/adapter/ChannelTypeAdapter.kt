package com.wiatec.boblive.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.wiatec.boblive.R
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.utils.Zoom

class ChannelTypeAdapter(val channelTypeInfoList: ArrayList<ChannelTypeInfo>)
    : RecyclerView.Adapter<ChannelTypeAdapterViewHolder>() {

    private var context: Context? = null
    private var onItemClickListener:OnItemClickListener? = null
    private var onItemFocusListener:OnItemFocusListener? = null

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
                .placeholder(R.drawable.img_channel_type_hold)
                .error(R.drawable.img_channel_type_hold)
                .dontAnimate()
                .into(holder.ivIcon)
        holder.itemView.setOnFocusChangeListener { v, hasFocus ->
            if(onItemFocusListener != null) {
                onItemFocusListener!!.onFocus(v, position, hasFocus)
            }
            if(hasFocus){
                Zoom.zoomIn10to11(v)
            }else{
                Zoom.zoomIn11to10(v)
            }
        }
        holder.itemView.setOnClickListener { v ->
            if(onItemClickListener != null){
                onItemClickListener!!.onClick(v, position)
            }
        }
    }

    override fun getItemCount(): Int {
        return channelTypeInfoList.size
    }

    interface OnItemFocusListener{
        fun onFocus(view: View, position: Int, hasFocus: Boolean)
    }

    fun setOnItemFocusListener(onItemFocusListener: OnItemFocusListener){
        this.onItemFocusListener = onItemFocusListener
    }

    interface OnItemClickListener{
        fun onClick(view: View, position: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

}