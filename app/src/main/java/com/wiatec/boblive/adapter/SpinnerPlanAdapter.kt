package com.wiatec.boblive.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

/**
 * Created by patrick on 16/01/2018.
 * create time : 4:01 PM
 */
class SpinnerPlanAdapter(context: Context, array: Array<String>): ArrayAdapter<String>(context, 0, array){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return convertView!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return super.getDropDownView(position, convertView, parent)
    }
}