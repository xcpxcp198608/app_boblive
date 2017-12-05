package com.wiatec.boblive.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.wiatec.boblive.R
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.pojo.VoucherUserCategoryInfo
import com.wiatec.boblive.utils.Zoom

class VoucherCategoryAdapter(private var voucherUserCategoryInfoList: ArrayList<VoucherUserCategoryInfo>)
    : RecyclerView.Adapter<VoucherCategoryAdapterViewHolder>() {

    private var context: Context? = null
    private var onItemClickListener:OnItemClickListener? = null
    private var onItemFocusListener:OnItemFocusListener? = null
    private var onItemLongClickListener:OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): VoucherCategoryAdapterViewHolder {
        context = parent!!.context
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_voucher_category, parent, false)
        return VoucherCategoryAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoucherCategoryAdapterViewHolder?, position: Int) {
        val voucherUserCategoryInfo = voucherUserCategoryInfoList[position]
        if (holder == null) return
        holder.tvPlan.text = voucherUserCategoryInfo.category
        holder.tvPrice.text = voucherUserCategoryInfo.price.toString()
        holder.tvDescription.text = voucherUserCategoryInfo.description
    }

    override fun getItemCount(): Int {
        return voucherUserCategoryInfoList.size
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

    interface OnItemLongClickListener{
        fun onLongClick(view: View, position: Int)
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener
    }

    fun notifyDataChange(voucherUserCategoryInfoList: ArrayList<VoucherUserCategoryInfo>){
        this.voucherUserCategoryInfoList = voucherUserCategoryInfoList
        notifyDataSetChanged()
    }

}