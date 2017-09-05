package com.wiatec.boblive.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.wiatec.boblive.instance.KEY_CHANNEL_LIST
import com.wiatec.boblive.instance.KEY_POSITION
import com.wiatec.boblive.R
import com.wiatec.boblive.instance.TYPE_CHANNEL
import com.wiatec.boblive.adapter.ChannelAdapter
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.presenter.ChannelPresenter
import kotlinx.android.synthetic.main.activity_channel.*
import java.io.Serializable

/**
 * Created by patrick on 13/07/2017.
 * create time : 10:19 AM
 */
class ChannelActivity : BaseActivity<IChannel, ChannelPresenter> (), IChannel {

    override fun createPresenter(): ChannelPresenter {
        return ChannelPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)
        val type: String = intent.getStringExtra(TYPE_CHANNEL)
        presenter!!.loadChannel(type)
        presenter!!.loadAdImage()
        btRetry.setOnClickListener { presenter!!.loadChannel(type) }
    }

    override fun onStart() {
        super.onStart()
        checkValidate(this)
    }

    override fun loadAdImage(execute: Boolean, imagePath: String?) {
        if(execute){

        }
    }

    override fun loadChannel(execute: Boolean, channelList: ArrayList<ChannelInfo>?) {
        if(!execute) {
            llLoading.visibility = View.VISIBLE
            tvLoading.text = getString(R.string.data_load_error)
            pbLoading.visibility = View.GONE
            btRetry.visibility = View.VISIBLE
            btRetry.requestFocus()
            return
        }
        llLoading.visibility = View.GONE
        tvPosition.text = "1"
        tvSplit.visibility = View.VISIBLE
        tvTotal.text = channelList!!.size.toString()
        val channelAdapter: ChannelAdapter = ChannelAdapter(channelList!!)
        rcvChannel.adapter = channelAdapter
        rcvChannel.layoutManager = GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false)
        channelAdapter.setOnItemClickListener(object: ChannelAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent: Intent = Intent(this@ChannelActivity, PlayActivity::class.java)
                intent.putExtra(KEY_CHANNEL_LIST, channelList as Serializable)
                intent.putExtra(KEY_POSITION, position)
                startActivity(intent)
            }
        })
        channelAdapter.setOnItemFocusListener(object: ChannelAdapter.OnItemFocusListener{
            override fun onFocus(view: View, position: Int, hasFocus: Boolean) {
                tvPosition.text = (position+1).toString()
            }
        })
    }
}