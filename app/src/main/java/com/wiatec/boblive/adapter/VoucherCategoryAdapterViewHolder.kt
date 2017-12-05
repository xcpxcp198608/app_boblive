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

class VoucherCategoryAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var tvPlan: TextView = itemView.findViewById(R.id.tvPlan) as TextView
    var tvPrice: TextView = itemView.findViewById(R.id.tvPrice) as TextView
    var tvDescription: TextView = itemView.findViewById(R.id.tvDescription) as TextView

}
