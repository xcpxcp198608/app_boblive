package com.wiatec.boblive.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.wiatec.boblive.R
import com.wiatec.boblive.TYPE_CHANNEL
import com.wiatec.boblive.adapter.ChannelTypeAdapter
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.presenter.ChannelTypePresenter
import com.wiatec.boblive.utils.Zoom
import kotlinx.android.synthetic.main.activity_channel_type.*

/**
 * Created by patrick on 13/07/2017.
 * create time : 10:18 AM
 */
class ChannelTypeActivity : BaseActivity<IChannelType, ChannelTypePresenter>() , IChannelType {
    override fun createPresenter(): ChannelTypePresenter {
        return ChannelTypePresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_type)
        presenter!!.loadChannelType()
        btRetry.setOnClickListener { presenter!!.loadChannelType() }
    }

    override fun loadAdImage(execute: Boolean, imagePath: String?) {
        if(execute){
            Glide.with(this@ChannelTypeActivity).load(imagePath).dontAnimate().into(ivBackground)
        }
    }

    override fun loadChannelType(execute: Boolean, channelTypeList: ArrayList<ChannelTypeInfo>?) {
        if(!execute){
            llLoading.visibility = View.VISIBLE
            tvLoading.text = getString(R.string.data_load_error)
            pbLoading.visibility = View.GONE
            btRetry.visibility = View.VISIBLE
            btRetry.requestFocus()
            return
        }else{
            llLoading.visibility = View.GONE
            val channelTypeAdapter: ChannelTypeAdapter = ChannelTypeAdapter(channelTypeList!!)
            rcvChannelType.adapter = channelTypeAdapter
            rcvChannelType.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                    false)
            channelTypeAdapter.setOnItemClickListener(object: ChannelTypeAdapter.OnItemClickListener{
                override fun onClick(view: View, position: Int) {
                    val intent: Intent = Intent(this@ChannelTypeActivity, ChannelActivity::class.java)
                    intent.putExtra(TYPE_CHANNEL, channelTypeList[position].name)
                    startActivity(intent)
                }
            })
            channelTypeAdapter.setOnItemFocusListener(object: ChannelTypeAdapter.OnItemFocusListener{
                override fun onFocus(view: View, position: Int, hasFocus: Boolean) {
                    if(hasFocus) {
                        Zoom.zoomIn10to11(view)
                        presenter!!.loadAdImage()
                    }else{
                        Zoom.zoomIn11to10(view)
                    }
                }
            })
        }
    }

}