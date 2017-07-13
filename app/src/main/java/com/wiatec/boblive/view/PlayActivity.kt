package com.wiatec.boblive.view

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.SurfaceHolder
import android.view.View
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.KEY_POSITION

import com.wiatec.boblive.R
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.utils.AESUtil
import kotlinx.android.synthetic.main.activity_play.*

class PlayActivity : AppCompatActivity(), SurfaceHolder.Callback{

    var channelInfoList: ArrayList<ChannelInfo>? = null
    var position: Int = -1
    var mediaPlayer: MediaPlayer? = null
    var surfaceHolder:SurfaceHolder? =null
    var channelInfo: ChannelInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        channelInfoList = intent.getSerializableExtra("channelInfoList") as ArrayList<ChannelInfo>?
        position = intent.getIntExtra(KEY_POSITION, -1)
        if(channelInfoList == null || position < 0){
            return
        }
        channelInfo = channelInfoList!![position]
        Logger.d(channelInfo.toString())
        surfaceHolder = surfaceView.holder
        surfaceHolder!!.addCallback(this@PlayActivity)
        surfaceHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if(channelInfo != null) {
            play(AESUtil.decrypt(channelInfo!!.url, AESUtil.KEY))
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        if(mediaPlayer != null){
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    private fun play(url: String){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer()
        }
        progressBar.visibility = View.VISIBLE
        mediaPlayer!!.reset()
        mediaPlayer!!.setDataSource(url)
        mediaPlayer!!.setDisplay(surfaceHolder)
        mediaPlayer!!.prepareAsync()
        mediaPlayer!!.setOnPreparedListener {
            progressBar.visibility = View.GONE
            mediaPlayer!!.start() }
        mediaPlayer!!.setOnErrorListener { _,_,_ ->
            play(AESUtil.decrypt(channelInfo!!.url, AESUtil.KEY))
            true
        }
        mediaPlayer!!.setOnCompletionListener { play(AESUtil.decrypt(channelInfo!!.url, AESUtil.KEY)) }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayer != null){
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_DPAD_LEFT || event.keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            position --
            if(position < 0){
                position = channelInfoList!!.size -1
            }
            channelInfo = channelInfoList!![position]
            play(AESUtil.decrypt(channelInfo!!.url , AESUtil.KEY))
        }
        if(event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || event.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT){
            position ++
            if(position >= channelInfoList!!.size){
                position = 0
            }
            channelInfo = channelInfoList!![position]
            play(AESUtil.decrypt(channelInfo!!.url , AESUtil.KEY))
        }
        return super.onKeyDown(keyCode, event)
    }
}
