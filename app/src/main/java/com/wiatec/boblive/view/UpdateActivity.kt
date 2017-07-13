package com.wiatec.boblive.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.*

import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.utils.OkHttp.Bean.DownloadInfo
import com.wiatec.boblive.utils.OkHttp.Listener.DownloadListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import kotlinx.android.synthetic.main.activity_update.*

class UpdateActivity : AppCompatActivity() {

    var url:String? = null
    var path:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        path = getExternalFilesDir("files").absolutePath+"/download"
        url = intent.getStringExtra(KEY_URL)
        if(url != null){
            startDownload()
        }
    }

    private fun startDownload() {
        OkMaster.download(this)
                .url(url)
                .name(packageName+".apk")
                .path(path)
                .startDownload(object :DownloadListener{
                    override fun onPending(downloadInfo: DownloadInfo?) {

                    }

                    override fun onStart(downloadInfo: DownloadInfo?) {
                        progressBar.visibility = View.VISIBLE
                        tvProgress.text = ZERO_PERCENT
                        progressBar.progress = 0
                    }

                    override fun onPause(downloadInfo: DownloadInfo?) {
                    }

                    override fun onProgress(downloadInfo: DownloadInfo?) {
                        tvProgress.text = downloadInfo!!.progress.toString()+ PERCENT
                        progressBar.progress = downloadInfo.progress
                    }

                    override fun onFinished(downloadInfo: DownloadInfo?) {
                        tvProgress.text = HUNDRED_PERCENT
                        progressBar.progress = 100
                        tvProgress.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        AppUtil.installApk(Application.context!!, downloadInfo!!.path, downloadInfo.name)
                    }

                    override fun onCancel(downloadInfo: DownloadInfo?) {
                    }

                    override fun onError(downloadInfo: DownloadInfo?) {
                        Logger.d(downloadInfo!!.message)
                    }
                })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_BACK){
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
