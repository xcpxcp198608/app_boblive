//package com.wiatec.boblive.view
//
//import android.app.Dialog
//import android.content.Intent
//import android.net.Uri
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.os.Message
//import android.support.v7.app.AlertDialog
//import android.view.KeyEvent
//import android.view.SurfaceHolder
//import android.view.View
//import android.view.Window
//import android.widget.Button
//import android.widget.RadioGroup
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.px.kotlin.utils.Logger
//import com.px.kotlin.utils.SPUtil
//
//import com.wiatec.boblive.R
//import com.wiatec.boblive.entity.CODE_OK
//import com.wiatec.boblive.entity.ResultInfo
//import com.wiatec.boblive.instance.*
//import com.wiatec.boblive.manager.PlayManager
//import com.wiatec.boblive.pojo.ChannelInfo
//import com.wiatec.boblive.utils.EmojiToast
//import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
//import com.wiatec.boblive.utils.OkHttp.OkMaster
//import com.wiatec.boblive.utils.SysUtil
//import kotlinx.android.synthetic.main.activity_player1.*
//import org.videolan.vlc.listener.MediaListenerEvent
//import java.io.IOException
//import java.text.DecimalFormat
//
//
//class Player1Activity : AppCompatActivity(), PlayManager.PlayListener, View.OnClickListener{
//
//    private var playManager: PlayManager? = null
//    private var currentUrlPosition: Int = 0
//    private var send = true
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_player1)
//        val channelInfoList: ArrayList<ChannelInfo> = intent.getSerializableExtra(KEY_CHANNEL_LIST)
//                as ArrayList<ChannelInfo>
//        val position: Int = intent.getIntExtra(KEY_POSITION, -1)
//        if(position < 0){
//            return
//        }
//        playManager = PlayManager(channelInfoList, position)
//        playManager!!.playListener = this
//        playManager!!.dispatchChannel()
//        flPlay.setOnClickListener(this)
//        ibtErrorReport.setOnClickListener(this)
//    }
//
//    override fun play(url: String) {
//        realPlay(handleUrl(url))
//    }
//
//    override fun jumpToAd() {
//        startActivity(Intent(this@Player1Activity, AdActivity::class.java))
//        finish()
//    }
//
//    private fun handleUrl(url: String): ArrayList<String> {
//        var urlList = ArrayList<String>()
//        if(!url.contains("#")){
//            urlList.add(url)
//        }else{
//            urlList = url.split("#") as ArrayList<String>
//        }
//        val urlList1 = ArrayList<String>()
//        for(u in urlList){
//            if (u.contains("protv.company")) {
//                val streamToken = SPUtil.get(this, "streamToken", "123") as String
//                val u1 = u.trim() + "?token=" + streamToken
//                urlList1.add(u1)
//            }else {
//                urlList1.add(u.trim())
//            }
//        }
//        return urlList1
//    }
//
//    private fun realPlay(urlList: ArrayList<String>) {
//        sendNetSpeed()
//        val url = urlList[currentUrlPosition]
//        Logger.d(url)
//        videoView.startPlay(url)
//        videoView.setMediaListenerEvent(object : MediaListenerEvent{
//            override fun eventError(error: Int, show: Boolean) {
//                Logger.d("eventError--"+show)
//                tvNetSpeed.visibility = View.VISIBLE
//                loopPlay(urlList)
//            }
//
//            override fun eventStop(isPlayError: Boolean) {
//                Logger.d("eventStop--" + isPlayError)
//            }
//
//            override fun eventPlay(isPlaying: Boolean) {
//                if(isPlaying){
//                    progressBar.visibility = View.GONE
//                    tvNetSpeed.visibility = View.GONE
//                }
//            }
//
//            override fun eventBuffing(buffing: Float, show: Boolean) {
//                Logger.d("eventBuffing--" + show)
//                if(show){
//                    progressBar.visibility = View.VISIBLE
//                    tvNetSpeed.visibility = View.VISIBLE
//                }else{
//                    progressBar.visibility = View.GONE
//                    tvNetSpeed.visibility = View.GONE
//                }
//            }
//
//            override fun eventPlayInit(openClose: Boolean) {
//                Logger.d("eventPlayInit--" + openClose)
//            }
//        })
//
//    }
//
//    private fun loopPlay(urlList: ArrayList<String>){
//        currentUrlPosition ++
//        if(currentUrlPosition >= urlList.size){
//            currentUrlPosition = 0
//        }
//        realPlay(urlList)
//    }
//
//    override fun onClick(v: View?) {
//        when(v!!.id){
//            R.id.flPlay -> {llController.visibility = View.VISIBLE
//                ibtErrorReport.requestFocus()}
//            R.id.ibtErrorReport -> showErrorReportDialog()
//            else -> {}
//        }
//    }
//
//    private fun release(){
//        videoView.onStop()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        release()
//        send = false
//    }
//
//    private fun showErrorReportDialog() {
//        var message = ""
//        val dialog: Dialog = AlertDialog.Builder(this).create()
//        dialog.show()
//        val window: Window = dialog.window
//        window.setContentView(R.layout.dialog_error_report)
//        val radioGroup: RadioGroup = window.findViewById(R.id.radioGroup) as RadioGroup
//        val btConfirm: Button = window.findViewById(R.id.btSend) as Button
//        radioGroup.check(R.id.rbMessage1)
//        radioGroup.setOnCheckedChangeListener { _, checkedId ->
//            message = when(checkedId){
//                R.id.rbMessage1 -> getString(R.string.error_msg1)
//                R.id.rbMessage2 -> getString(R.string.error_msg2)
//                R.id.rbMessage3 -> getString(R.string.error_msg3)
//                else -> getString(R.string.error_msg1)
//            }
//        }
//        btConfirm.setOnClickListener {
//            sendErrorReport(message)
//            dialog.dismiss()
//        }
//    }
//
//    private fun sendErrorReport(message: String){
//        OkMaster.post(URL_ERROR_REPORT_SEND)
//                .parames("userName", SPUtil.get(Application.context!!, KEY_AUTHORIZATION, "test") as String)
//                .parames("channelName", playManager!!.channelInfo!!.name)
//                .parames("message", message)
//                .enqueue(object : StringListener(){
//                    override fun onSuccess(s: String?) {
//                        val resultInfo: ResultInfo<String> = Gson().fromJson(s,
//                                object: TypeToken<ResultInfo<String>>(){}.type)
//                        if (resultInfo.code == CODE_OK) {
//                            EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SMILE)
//                        } else {
//                            EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
//                        }
//                    }
//
//                    override fun onFailure(e: String?) {
//                        if(e != null) Logger.d(e)
//                    }
//                })
//    }
//
//    private fun sendNetSpeed() {
//        Thread(Runnable {
//            while (send) {
//                val s1 = SysUtil.getNetSpeedBytes()
//                try {
//                    Thread.sleep(2000)
//                } catch (e: InterruptedException) {
//                    e.printStackTrace()
//                }
//
//                val s2 = SysUtil.getNetSpeedBytes()
//                val f = (s2 - s1).toFloat() / 2f / 1024f
//                val decimalFormat = DecimalFormat("##0.00")
//                val s = decimalFormat.format(f)
//                val m = handler.obtainMessage()
//                m.obj = s
//                handler.sendMessage(m)
//            }
//        }).start()
//    }
//
//    private val handler = object : Handler(Looper.getMainLooper()) {
//        override fun handleMessage(msg: Message) {
//            super.handleMessage(msg)
//            val s = msg.obj.toString()
////            Logger.d(s)
//            tvNetSpeed.text = s + "kbs"
//        }
//    }
//
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if(event!!.keyCode == KeyEvent.KEYCODE_DPAD_UP || event.keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS){
//            playManager!!.previousChannel()
//        }
//        if(event.keyCode == KeyEvent.KEYCODE_DPAD_DOWN || event.keyCode == KeyEvent.KEYCODE_MEDIA_NEXT){
//            playManager!!.nextChannel()
//        }
//        if(event.keyCode == KeyEvent.KEYCODE_BACK){
//            if(llController.visibility == View.VISIBLE) {
//                llController.visibility = View.GONE
//                return true
//            }
//        }
//        return super.onKeyDown(keyCode, event)
//    }
//
//}
