package com.wiatec.boblive.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wiatec.boblive.Application
import com.wiatec.boblive.R
import com.wiatec.boblive.pojo.AppInfo
import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.utils.Zoom

/**
 * Created by patrick on 31/07/2017.
 * create time : 9:59 AM
 */
class AppAdapter(val appList:List<AppInfo>): RecyclerView.Adapter<AppViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var onItemFocusListener: OnItemFocusListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AppViewHolder {
        val view: View = LayoutInflater.from(parent!!.context).inflate(R.layout.item_app,
                parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder?, position: Int) {
        if(holder == null) return
        val appInfo = appList[position]
        holder.textView.text = AppUtil.getLabelName(Application.context!!, appInfo.packageName)
        holder.imageView.setImageDrawable(AppUtil.getIcon(Application.context!!, appInfo.packageName))
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
        return appList.size
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