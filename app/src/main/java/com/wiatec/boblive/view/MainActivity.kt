package com.wiatec.boblive.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.px.kotlin.utils.Logger

import com.wiatec.boblive.presenter.MainPresenter
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.*
import com.wiatec.boblive.adapter.ChannelTypeAdapter
import com.wiatec.boblive.entity.CODE_OK
import com.wiatec.boblive.entity.ResultInfo
import com.wiatec.boblive.pojo.AuthorizationInfo
import com.wiatec.boblive.pojo.ChannelTypeInfo
import com.wiatec.boblive.pojo.UpgradeInfo
import com.wiatec.boblive.utils.AppUtil
import com.wiatec.boblive.utils.EmojiToast
import com.wiatec.boblive.utils.NetUtil
import com.wiatec.boblive.utils.Zoom
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<IMain, MainPresenter>(), IMain, View.OnFocusChangeListener {

    var isFirstBoot: Boolean = true

    override fun createPresenter(): MainPresenter {
        return MainPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirstBoot = SPUtil.get(this@MainActivity, KEY_FIRST_BOOT, true) as Boolean
        if(isFirstBoot){
            startActivity(Intent(this@MainActivity, LanguageSettingActivity::class.java))
            finish()
        }else {
            setContentView(R.layout.activity_main)
            authorization()
            initChannelType()
            btMenu.setOnClickListener { startActivity(Intent(this, AppsActivity::class.java)) }
            btMenu.onFocusChangeListener = this
        }
    }

    override fun onStart() {
        super.onStart()
        presenter!!.checkUpgrade()
        presenter!!.loadAdImage()
    }

    fun makeChannelTypeData(): ArrayList<ChannelTypeInfo> {
        val c1 = ChannelTypeInfo(0, CHANNEL_TYPE_BASIC, "", 1, 0)
        val c2 = ChannelTypeInfo(0, CHANNEL_TYPE_PREMIUM, "", 1, 0)
        val c3 = ChannelTypeInfo(0, CHANNEL_TYPE_ADULT, "", 1, 0)
        val channelTypeList = ArrayList<ChannelTypeInfo>()
        channelTypeList.add(c1)
        channelTypeList.add(c2)
        channelTypeList.add(c3)
        return channelTypeList
    }

    fun initChannelType(){
        val channelTypeList = makeChannelTypeData()
        val channelTypeAdapter = ChannelTypeAdapter(channelTypeList)
        rcvMain.adapter = channelTypeAdapter
        rcvMain.layoutManager = LinearLayoutManager(this@MainActivity,
                LinearLayoutManager.HORIZONTAL, false)
        channelTypeAdapter.setOnItemClickListener(object: ChannelTypeAdapter.OnItemClickListener{
            override fun onClick(view: View, position: Int) {
                val authorization: String = SPUtil.get(this@MainActivity, KEY_AUTHORIZATION, "").toString()
                if(TextUtils.isEmpty(authorization)){
                    showAuthorizationDialog()
                    return
                }
                val intent: Intent = Intent(this@MainActivity, ChannelActivity::class.java)
                intent.putExtra(TYPE_CHANNEL, channelTypeList[position].name)
                startActivity(intent)
            }
        })
    }

    override fun checkUpgrade(execute: Boolean, upgradeInfo: UpgradeInfo?) {
        if(execute){
            showUpgradeDialog(upgradeInfo!!)
        }
    }

    override fun loadAdImage(execute: Boolean, imagePath: String?) {
//        if(execute){
//            Glide.with(this@MainActivity).load(imagePath).dontAnimate().into(ivMain)
//        }
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
        val tvInfo: TextView = window.findViewById(R.id.tvInfo) as TextView
        val btConfirm: Button = window.findViewById(R.id.btConfirm) as Button
        val btCancel: Button = window.findViewById(R.id.btCancel) as Button
        tvInfo.text = upgradeInfo.info
        btConfirm.setOnClickListener {
            val intent:Intent = Intent(this, UpgradeActivity::class.java)
            intent.putExtra("url", upgradeInfo.url)
            startActivity(intent)}
        btCancel.setOnClickListener { finish() }
    }

    /**
     *  Is there key active system ?
     *  YES: validate
     *  NO:  showAuthorizationDialog
     */
    private fun authorization() {
        val authorization: String = SPUtil.get(this, KEY_AUTHORIZATION, "").toString()
        Logger.d(authorization)
        if(TextUtils.isEmpty(authorization)){
            showAuthorizationDialog()
            return
        }else{
            presenter!!.validateAuthorization(authorization)
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
        val etAuthorization: EditText = window.findViewById(R.id.etAuthorization) as EditText
        val btConfirm:Button = window.findViewById(R.id.btConfirm) as Button
        btConfirm.setOnClickListener {
            val activeKey = etAuthorization.text.toString()
            if(activeKey != "wiatec" && (TextUtils.isEmpty(activeKey) || activeKey.length < 16)){
                EmojiToast.show(getString(R.string.error_key_format), EmojiToast.EMOJI_SAD)
            }else {
                if(!NetUtil.isConnected){
                    EmojiToast.show(getString(R.string.no_network), EmojiToast.EMOJI_SAD)
                    AppUtil.launchApp(this@MainActivity, PACKAGE_NAME_SETTINGS)
                }else {
                    presenter!!.activeAuthorization(activeKey)
                }
                dialog.dismiss()

            }
        }
    }

    override fun activeAuthorization(execute: Boolean, resultInfo: ResultInfo<AuthorizationInfo>?) {
        if(!execute) return
        Logger.d(resultInfo!!)
        if(resultInfo.code == CODE_OK){
            val authorizationInfo:AuthorizationInfo = resultInfo.data[0]
            EmojiToast.show(getString(R.string.active_success), EmojiToast.EMOJI_SMILE)
            SPUtil.put(Application.context!!, KEY_AUTHORIZATION, authorizationInfo.key!!)
            SPUtil.put(Application.context!!, KEY_LEVEL, authorizationInfo.level.toString())
        }else{
            EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
            showAuthorizationDialog()
        }
    }

    override fun validateAuthorization(execute: Boolean, resultInfo: ResultInfo<AuthorizationInfo>?) {
        if(!execute) return
        Logger.d(resultInfo!!)
        if(resultInfo.code == CODE_OK){
            val authorizationInfo:AuthorizationInfo = resultInfo.data[0]
            SPUtil.put(Application.context!!, KEY_AUTHORIZATION, authorizationInfo.key!!)
            SPUtil.put(Application.context!!, KEY_LEVEL, authorizationInfo.level.toString())
            SPUtil.put(Application.context!!, KEY_EXPERIENCE, resultInfo.message)
            if(authorizationInfo.level <= 0) {
                showAuthorizationDialog()
                EmojiToast.show(getString(R.string.authorization_error), EmojiToast.EMOJI_SAD)
            }
        }else{
            EmojiToast.show(resultInfo.message, EmojiToast.EMOJI_SAD)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.keyCode == KeyEvent.KEYCODE_BACK || event.keyCode == KeyEvent.KEYCODE_HOME) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if(v == null) return
        if(hasFocus){
            Zoom.zoomIn10to11(v)
        }else{
            Zoom.zoomIn11to10(v)
        }
    }
}
