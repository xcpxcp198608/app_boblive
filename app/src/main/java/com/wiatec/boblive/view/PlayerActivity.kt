package com.wiatec.boblive.view

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AlertDialog
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioGroup
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.R
import com.wiatec.boblive.entity.CODE_OK
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.instance.Application
import com.wiatec.boblive.instance.KEY_AUTHORIZATION
import com.wiatec.boblive.instance.URL_ERROR_REPORT_SEND
import com.wiatec.boblive.manager.PlayManager
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.utils.EmojiToast
import com.wiatec.boblive.utils.LibUtil
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import com.wiatec.boblive.utils.SysUtil
import kotlinx.android.synthetic.main.activity_player.*
import org.videolan.libvlc.IVLCVout
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import java.text.DecimalFormat

class PlayerActivity : AppCompatActivity() , IVLCVout.Callback, MediaPlayer.EventListener,
        View.OnClickListener, PlayManager.PlayListener{
    private var vlcVout: IVLCVout? = null
    private var mediaPlayer: MediaPlayer? = null
    private val libvlc = LibUtil.getLibVLC(null)
    private var urlList = ArrayList<String>()
    private var urlPosition = 0
    private var send = true
    private var playManager: PlayManager? = null
    private var media: Media? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_player)
        val intent = intent ?: return
        val channelInfoList = intent.getSerializableExtra("channelInfoList") as List<ChannelInfo>
        val position = intent.getIntExtra("position", 0)
        playManager = PlayManager(channelInfoList, position)
        playManager!!.playListener = this
        flPlay.setOnClickListener(this)
        ibtErrorReport.setOnClickListener(this)
        sendNetSpeed()
        init()
    }

    private fun init(){
        val surfaceHolder = surfaceView!!.holder
        surfaceHolder.setKeepScreenOn(true)
        mediaPlayer = MediaPlayer(libvlc)
        vlcVout = mediaPlayer!!.vlcVout
        vlcVout!!.addCallback(this)
        vlcVout!!.setVideoView(surfaceView)
        vlcVout!!.attachViews()
        playManager!!.dispatchChannel()
    }

    override fun play(url: String) {
        urlPosition = 0
        urlList = handleUrl(url)
        playVideo(urlList[urlPosition])
    }

    override fun jumpToAd() {
        startActivity(Intent(this@PlayerActivity, AdActivity::class.java))
        finish()
    }

    private fun handleUrl(url: String): ArrayList<String> {
        var urlList1 = ArrayList<String>()
        if(url.contains("#")){
            urlList1 = url.split("#") as ArrayList<String>
        }else{
            urlList1.add(url)
        }
        val urlList2 = ArrayList<String>()
        for(u in urlList1){
            if (u.contains("protv.company")) {
                val streamToken = SPUtil.get(this, "streamToken", "123") as String
                val u1 = u.trim() + "?token=" + streamToken
                urlList2.add(u1)
            }else {
                urlList2.add(u.trim())
            }
        }
        return urlList2
    }

    private fun playVideo(url: String){
        try {
            media = null
            if(media == null) {
                media = Media(libvlc, Uri.parse(url))
            }
            mediaPlayer!!.media = media
            mediaPlayer!!.setEventListener(this)
            mediaPlayer!!.play()
        } catch (e: Exception) {
            Logger.d(e.toString())
        }
    }

    private fun playNextUrl(){
        urlPosition += 1
        if(urlPosition >= urlList.size){
            urlPosition = 0
        }
        media!!.release()
        media = null
        playVideo(urlList[urlPosition])
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
            send = false
            release()
        } catch (e: Exception) {
            Logger.d(e.toString())
        }
    }

    private fun release() {
        mediaPlayer!!.stop()
        vlcVout!!.detachViews()
        vlcVout!!.removeCallback(this)
        mediaPlayer!!.setEventListener(null)
        mediaPlayer!!.release()
    }

    override fun onSurfacesCreated(p0: IVLCVout?) {
    }

    override fun onSurfacesDestroyed(p0: IVLCVout?) {
    }

    override fun onNewLayout(p0: IVLCVout?, p1: Int, p2: Int, p3: Int, p4: Int, p5: Int, p6: Int) {
    }

    override fun onEvent(p0: MediaPlayer.Event?) {
        try {
            if (mediaPlayer!!.playerState == Media.State.Opening) {
                progressBar.visibility = View.VISIBLE
            }
            if (mediaPlayer!!.playerState == Media.State.Playing) {
                progressBar.visibility = View.GONE
                tvNetSpeed.visibility = View.GONE
            }
            if (mediaPlayer!!.playerState == Media.State.Buffering) {
                Logger.d("buffering")
                progressBar.visibility = View.VISIBLE
                tvNetSpeed.visibility = View.VISIBLE
            }
            if (mediaPlayer!!.playerState == Media.State.Error) {
                Logger.d("error")
                tvNetSpeed.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                mediaPlayer!!.time = 0
                mediaPlayer!!.stop()
                playNextUrl()
            }
            if (mediaPlayer!!.playerState == Media.State.Ended) {
                Logger.d("end")
                tvNetSpeed.visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                mediaPlayer!!.time = 0
                mediaPlayer!!.stop()
//                playNextUrl()
            }
        } catch (e: Exception) {
            Logger.d(e.toString())
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.flPlay -> {llController.visibility = View.VISIBLE
                ibtErrorReport.requestFocus()}
            R.id.ibtErrorReport -> showErrorReportDialog()
            else -> {}
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_DPAD_UP || event.keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            playManager!!.previousChannel()
        }
        if(event.keyCode == KeyEvent.KEYCODE_DPAD_DOWN || event.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT){
            playManager!!.nextChannel()
        }
        if(event.keyCode == KeyEvent.KEYCODE_BACK){
            if(llController.visibility == View.VISIBLE) {
                llController.visibility = View.GONE
                return true
            }else{
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showErrorReportDialog() {
        var message = ""
        val dialog: Dialog = AlertDialog.Builder(this).create()
        dialog.show()
        val window: Window = dialog.window
        window.setContentView(R.layout.dialog_error_report)
        val radioGroup: RadioGroup = window.findViewById(R.id.radioGroup) as RadioGroup
        val btConfirm: Button = window.findViewById(R.id.btSend) as Button
        radioGroup.check(R.id.rbMessage1)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            message = when(checkedId){
                R.id.rbMessage1 -> getString(R.string.error_msg1)
                R.id.rbMessage2 -> getString(R.string.error_msg1)
                R.id.rbMessage3 -> getString(R.string.error_msg1)
                else -> getString(R.string.error_msg1)
            }
        }
        btConfirm.setOnClickListener {
            sendErrorReport(message)
            dialog.dismiss()
        }
    }

    private fun sendErrorReport(message: String){
        OkMaster.post(URL_ERROR_REPORT_SEND)
                .parames("userName", SPUtil.get(Application.context!!, KEY_AUTHORIZATION, "test") as String)
                .parames("channelName", playManager!!.channelInfo!!.name)
                .parames("message", message)
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        val resultInfo: ResultInfo<String> = Gson().fromJson(s,
                                object: TypeToken<ResultInfo<String>>(){}.type)
                        if (resultInfo.code == CODE_OK) {
                            EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SMILE)
                        } else {
                            EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
                        }
                    }

                    override fun onFailure(e: String?) {
                        if(e != null) Logger.d(e)
                    }
                })
    }

    private fun sendNetSpeed() {
        Thread(Runnable {
            while (send) {
                val s1 = SysUtil.getNetSpeedBytes()
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                val s2 = SysUtil.getNetSpeedBytes()
                val f = (s2 - s1).toFloat() / 2f / 1024f
                val decimalFormat = DecimalFormat("##0.00")
                val s = decimalFormat.format(f)
                val m = handler.obtainMessage()
                m.obj = s
                handler.sendMessage(m)
            }
        }).start()
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val s = msg.obj.toString()
            tvNetSpeed.text = s + "kbs"
        }
    }
}