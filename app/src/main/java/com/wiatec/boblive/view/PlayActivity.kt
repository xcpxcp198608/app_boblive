package com.wiatec.boblive.view

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.KeyEvent
import android.view.SurfaceHolder
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import com.px.kotlin.utils.Logger

import com.wiatec.boblive.R
import com.wiatec.boblive.manager.PlayManager
import com.wiatec.boblive.pojo.ChannelInfo
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import kotlinx.android.synthetic.main.activity_play.*
import com.wiatec.boblive.utils.EmojiToast
import com.wiatec.boblive.entity.ResultInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.entity.CODE_OK
import com.wiatec.boblive.instance.*


class PlayActivity : AppCompatActivity(), SurfaceHolder.Callback, PlayManager.PlayListener,
        View.OnClickListener{

    private var mediaPlayer: MediaPlayer? = null
    private var surfaceHolder:SurfaceHolder? =null
    private var playManager: PlayManager? = null
    private var currentUrlPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        val channelInfoList: ArrayList<ChannelInfo> = intent.getSerializableExtra(KEY_CHANNEL_LIST) as ArrayList<ChannelInfo>
        val position: Int = intent.getIntExtra(KEY_POSITION, -1)
        if(position < 0){
            return
        }
        playManager = PlayManager(channelInfoList, position)
        playManager!!.playListener = this
        surfaceHolder = surfaceView.holder
        surfaceHolder!!.addCallback(this@PlayActivity)
        surfaceHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        flPlay.setOnClickListener(this)
        ibtErrorReport.setOnClickListener(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        playManager!!.dispatchChannel()
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

    override fun play(url: String) {
        playChannel(handleUrl(url))
    }

    override fun jumpToAd() {
        startActivity(Intent(this@PlayActivity, AdActivity::class.java))
        finish()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.flPlay -> {llController.visibility = View.VISIBLE
                            ibtErrorReport.requestFocus()}
            R.id.ibtErrorReport -> showErrorReportDialog()
            else -> {}
        }
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

    private fun handleUrl(url: String): ArrayList<String> {
        var urlList = ArrayList<String>()
        if(!url.contains("#")){
            urlList.add(url)
        }else{
            urlList = url.split("#") as ArrayList<String>
        }
//        Logger.d(urlList)
        return urlList
    }

    private fun playChannel(urlList: ArrayList<String>){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer()
        }
//        Logger.d(url)
        progressBar.visibility = View.VISIBLE
        mediaPlayer!!.reset()
        mediaPlayer!!.setDataSource(urlList[currentUrlPosition])
        mediaPlayer!!.setDisplay(surfaceHolder)
        mediaPlayer!!.prepareAsync()
        mediaPlayer!!.setOnPreparedListener {
            progressBar.visibility = View.GONE
            mediaPlayer!!.start() }
        mediaPlayer!!.setOnErrorListener { _,_,_ ->
            Logger.d("error")
            loopPlay(urlList)
            true
        }
        mediaPlayer!!.setOnCompletionListener {
            Logger.d("complete")
            loopPlay(urlList)
        }
    }

    private fun loopPlay(urlList: ArrayList<String>){
        currentUrlPosition ++
        if(currentUrlPosition >= urlList.size){
            currentUrlPosition = 0
        }
        playChannel(urlList)
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
            playManager!!.previousChannel()
        }
        if(event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || event.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT){
            playManager!!.nextChannel()
        }
        if(event.keyCode == KeyEvent.KEYCODE_BACK){
            if(llController.visibility == View.VISIBLE) {
                llController.visibility = View.GONE
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}
