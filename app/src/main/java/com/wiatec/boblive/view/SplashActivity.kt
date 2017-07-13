package com.wiatec.boblive.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.*
import com.wiatec.boblive.entity.CODE_OK
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.pojo.UpgradeInfo
import com.wiatec.boblive.presenter.SplashPresenter
import com.wiatec.boblive.utils.EmojiToast
import com.wiatec.boblive.utils.OkHttp.Listener.StringListener
import com.wiatec.boblive.utils.OkHttp.OkMaster
import com.wiatec.boblive.utils.SysUtil

/**
 * splash activity:
 * 1.check: is there app update
 * 2.check: is there user login
 */
class SplashActivity : BaseActivity<ISplashActivity, SplashPresenter>(), ISplashActivity {

    override fun createPresenter(): SplashPresenter {
        return SplashPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        if(presenter != null) {
            presenter!!.checkUpgrade()
        }
    }

    /**
     * check the app update
     * YES: show update confirm dialog
     * NO:  run checkUser()
     */
    override fun checkUpgrade(update: Boolean, upgradeInfo: UpgradeInfo) {
        if(update){
            showUpgradeDialog(upgradeInfo)
        }else{
            authorization()
        }
    }

    /**
     * show update dialog
     * Confirm: start update activity and start download
     * Cancel:  exit app
     */
    fun showUpgradeDialog(upgradeInfo: UpgradeInfo){
        val dialog: Dialog = AlertDialog.Builder(this).create()
        dialog.show()
        dialog.setCancelable(false)
        val window: Window = dialog.window
        window.setContentView(R.layout.dialog_update)
        val tvInfo:TextView = window.findViewById(R.id.tvInfo) as TextView
        val btConfirm:Button = window.findViewById(R.id.btConfirm) as Button
        val btCancel:Button = window.findViewById(R.id.btCancel) as Button
        tvInfo.text = upgradeInfo.info
        btConfirm.setOnClickListener {
            val intent:Intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("url", upgradeInfo.url)
            startActivity(intent)}
        btCancel.setOnClickListener { finish() }
    }

    /**
     *  Is there key active system ?
     *  YES: start MainActivity
     *  NO:  show login dialog
     */
    private fun authorization() {
        val authorization: String = SPUtil.get(this, "authorization", "").toString()
        if(TextUtils.isEmpty(authorization)){
            showAuthorizationDialog()
            return
        }else{
            validateAuthorization(authorization)
        }
    }

    /**
     * show authorization dialog for user type int Active Key
     * Confirm: checkAuthorization
     */
    private fun showAuthorizationDialog() {
        val dialog: Dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.show()
        dialog.setCancelable(false)
        val window: Window = dialog.window
        window.setContentView(R.layout.dialog_authorization)
        val etAuthorization:EditText = window.findViewById(R.id.etAuthorization) as EditText
        val btConfirm:Button = window.findViewById(R.id.btConfirm) as Button
        btConfirm.setOnClickListener {
            val activeKey = etAuthorization.text.toString()
            if(TextUtils.isEmpty(activeKey) || activeKey.length < 16){
                EmojiToast.show(getString(R.string.error_key_format), EmojiToast.EMOJI_SAD)
            }else {
                activeAuthorization(activeKey)
            }
        }
    }

    fun activeAuthorization(authorization: String){
        OkMaster.post(URL_ACTIVE)
                .parames(KEY_KEY, authorization)
                .parames(KEY_MAC, SysUtil.getWifiMac())
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        if(s == null){
                            EmojiToast.show(getString(R.string.error_server), EmojiToast.EMOJI_SAD)
                            return
                        }
                        val resultInfo:ResultInfo<AuthorizationInfo> = Gson().fromJson(s, object : TypeToken<ResultInfo<AuthorizationInfo>>(){}.type)
                        Logger.d(resultInfo)
                        if(resultInfo.code == CODE_OK){
                            val authorizationInfo:AuthorizationInfo = resultInfo.data[0]
                            SPUtil.put(Application.context!!, KEY_AUTHORIZATION, authorizationInfo.key)
                            SPUtil.put(Application.context!!, KEY_LEVEL, authorizationInfo.level.toString())
                            startActivity(Intent(Application.context!!, MainActivity::class.java))
                            finish()
                        }else{
                            EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
                        }
                    }

                    override fun onFailure(e: String?) {
                        if(e != null) Logger.d(e)
                        EmojiToast.show(getString(R.string.error_server), EmojiToast.EMOJI_SAD)
                    }
                })
    }

    private fun validateAuthorization(authorization: String) {
        OkMaster.post(URL_VALIDATE)
                .parames(KEY_KEY, authorization)
                .parames(KEY_MAC, SysUtil.getWifiMac())
                .enqueue(object : StringListener(){
                    override fun onSuccess(s: String?) {
                        if(s == null){
                            EmojiToast.show(getString(R.string.error_server), EmojiToast.EMOJI_SAD)
                            return
                        }
                        val resultInfo:ResultInfo<AuthorizationInfo> = Gson().fromJson(s, object : TypeToken<ResultInfo<AuthorizationInfo>>(){}.type)
                        Logger.d(resultInfo)
                        if(resultInfo.code == CODE_OK){
                            val authorizationInfo:AuthorizationInfo = resultInfo.data[0]
                            SPUtil.put(Application.context!!, KEY_AUTHORIZATION, authorizationInfo.key)
                            SPUtil.put(Application.context!!, KEY_LEVEL, authorizationInfo.level.toString())
                            SPUtil.put(Application.context!!, KEY_EXPERIENCE, resultInfo.message)
                            if(authorizationInfo.level > 0) {
                                startActivity(Intent(Application.context!!, MainActivity::class.java))
                                finish()
                            }else{
                                EmojiToast.show(getString(R.string.authorization_error), EmojiToast.EMOJI_SAD)
                            }
                        }else{
                            EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
                        }
                    }

                    override fun onFailure(e: String?) {
                        if(e != null) Logger.d(e)
                        EmojiToast.show(getString(R.string.error_server), EmojiToast.EMOJI_SAD)
                    }
                })
    }
}
