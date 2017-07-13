package com.wiatec.boblive.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView

import com.wiatec.boblive.R
import com.wiatec.boblive.adapter.ChannelTypeAdapter
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.TYPE_APP
import com.wiatec.boblive.TYPE_LIVE
import com.wiatec.boblive.TYPE_RADIO
import com.wiatec.boblive.adapter.ChannelAdapter
import com.wiatec.boblive.pojo.ChannelInfo
import org.jetbrains.anko.forEachChild
import java.io.Serializable


class MainActivity : BaseActivity<IMainActivity, MainPresenter>(), IMainActivity {

    var isLock: Int = 0

    override fun createPresenter(): MainPresenter {
        return MainPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter!!.loadChannelType()
    }

    override fun loadChannelType(channelTypeInfoList: ArrayList<ChannelTypeInfo>) {
        val channelTypeAdapter = ChannelTypeAdapter(channelTypeInfoList, object: ChannelTypeAdapter.OnItemSelectListener{
            override fun onItemSelected(view: View, position: Int, hasFocus: Boolean) {
                rcvChannelType.forEachChild { v ->  v.setBackgroundResource(R.drawable.bg_item_channel_type)}
                view.setBackgroundResource(R.drawable.bg_item_channel_type_focus)
                if (hasFocus) {
                    val channelTypeInfo = channelTypeInfoList[position]
                    isLock = channelTypeInfo.isLock
                    val country = channelTypeInfo.name
                    rcvChannel.visibility = View.GONE
                    tvLoading.visibility = View.VISIBLE
                    tvLoading.text = getString(R.string.data_loading)
                    if (channelTypeInfo.flag == 0) {
                        presenter!!.loadChannel(country)
                    }
                    SPUtil.put(this@MainActivity, "channelType", country)
                }
            }
        })
        rcvChannelType.adapter = channelTypeAdapter
        rcvChannelType.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun loadChannel(channelInfoList: ArrayList<ChannelInfo>) {
        val country:String = SPUtil.get(this@MainActivity, "channelType", "") as String
        if(country == channelInfoList[0].country){
            rcvChannel.visibility = View.VISIBLE
            tvLoading.visibility = View.GONE
            val channelAdapter: ChannelAdapter = ChannelAdapter(channelInfoList, object : ChannelAdapter.OnItemClickListener{
                override fun onItemClick(view: View, position: Int) {
                    play(channelInfoList , position)
                }
            })
            rcvChannel.adapter = channelAdapter
            rcvChannel.layoutManager = GridLayoutManager(this@MainActivity, 5)
        }
    }

    private fun play(channelInfoList: ArrayList<ChannelInfo>, position: Int){
        val channelInfo = channelInfoList[position]
        if(TYPE_LIVE == channelInfo.type){
            val intent:Intent = Intent(this@MainActivity, PlayActivity::class.java)
            intent.putExtra("channelInfoList", channelInfoList as Serializable)
            intent.putExtra("position", position)
            startActivity(intent)
        }else if(TYPE_RADIO == channelInfo.type){

        }else if(TYPE_APP == channelInfo.type){

        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showExitDialog() {
        val dialog: Dialog = AlertDialog.Builder(this).create()
        dialog.show()
        dialog.setCancelable(false)
        val window: Window = dialog.window
        window.setContentView(R.layout.dialog_update)
        val tvInfo: TextView = window.findViewById(R.id.tvInfo) as TextView
        val btConfirm: Button = window.findViewById(R.id.btConfirm) as Button
        val btCancel: Button = window.findViewById(R.id.btCancel) as Button
        tvInfo.text = getString(R.string.confirm_exit)
        btConfirm.setOnClickListener { finish()}
        btCancel.setOnClickListener { dialog.dismiss() }
    }
}
