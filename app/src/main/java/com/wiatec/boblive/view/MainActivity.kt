package com.wiatec.boblive.view

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
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
import com.wiatec.boblive.*
import com.wiatec.boblive.adapter.ChannelTypeAdapter
import com.wiatec.boblive.instance.*
import kotlinx.android.synthetic.main.activity_main.*
import android.view.WindowManager
import com.wiatec.boblive.pojo.*
import com.wiatec.boblive.task.PlayTokenTask
import com.wiatec.boblive.utils.*
import com.px.kotlin.utils.SPUtil
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer


class MainActivity : BaseActivity<IMain, MainPresenter>(), IMain, View.OnFocusChangeListener {

    private var isFirstBoot: Boolean = true
    private var disposable: Disposable? = null
    private var showDaysAnimate = false

    override fun createPresenter(): MainPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirstBoot = SPUtil.get(this@MainActivity, KEY_FIRST_BOOT, true) as Boolean
        if(isFirstBoot){
            startActivity(Intent(this@MainActivity, LanguageSettingActivity::class.java))
            finish()
        }else {
            setContentView(R.layout.activity_main)
            showAgreementDialog()
            initChannelType()
            showTimeAndData()
            tvVersion.text = AppUtil.getVersionName(this@MainActivity, packageName)
            btMenu.setOnClickListener { startActivity(Intent(this, AppsActivity::class.java)) }
            btSetting.setOnClickListener { AppUtil.launchApp(this, PACKAGE_NAME_SETTINGS) }
            btPerson.setOnClickListener { showAuthorizationDialog() }
            btMenu.onFocusChangeListener = this
            btSetting.onFocusChangeListener = this
            btPerson.onFocusChangeListener = this
        }
    }

    override fun onStart() {
        super.onStart()
        Thread(PlayTokenTask()).start()
        checkValidate(this)
        presenter!!.checkUpgrade()
        presenter!!.loadAdImage()
        showVoucherNoticeWillOutExpires()
    }

    private fun showVoucherNoticeWillOutExpires(){
        val leftMillsSeconds = SPUtil.get(KEY_VOUCHER_LEFT_MILLS_SECOND, 0L) as Long
        if (leftMillsSeconds in 1..259200000) {
            val alertDialog = AlertDialog.Builder(this, R.style.dialog).create()
            alertDialog.setCancelable(false)
            alertDialog.show()
            val window = alertDialog.window ?: return
            window.setContentView(R.layout.dialog_notice)
            val tvInfo = window.findViewById(R.id.tvInfo) as TextView
            val btnConfirm = window.findViewById(R.id.btConfirm)
            tvInfo.text = getString(R.string.out_expires)
            btnConfirm.setOnClickListener { alertDialog.dismiss() }
        }
    }

    private fun makeChannelTypeData(auth: String): ArrayList<ChannelTypeInfo> {
        val channelTypeList = ArrayList<ChannelTypeInfo>()
        if(TextUtils.isEmpty(auth)) {
            val c0 = ChannelTypeInfo(0, getString(R.string.mini_tag), getString(R.string.mini), "", "", 1, 0)
            channelTypeList.add(c0)
        }else {
            val isVoucher = SPUtil.get(KEY_IS_VOUCHER, false) as Boolean
            if(isVoucher){
                val c1 = ChannelTypeInfo(0, getString(R.string.basic_tag), getString(R.string.basic), "", "", 1, 0)
                val c2 = ChannelTypeInfo(0, getString(R.string.premium_tag), getString(R.string.premium), "", "", 1, 0)
                val c3 = ChannelTypeInfo(0, getString(R.string.film_tag), getString(R.string.film), "", "", 1, 0)
                channelTypeList.add(c1)
                channelTypeList.add(c2)
                channelTypeList.add(c3)
            }else {
                val c1 = ChannelTypeInfo(0, getString(R.string.basic_tag), getString(R.string.basic), "", "", 1, 0)
                val c2 = ChannelTypeInfo(0, getString(R.string.premium_tag), getString(R.string.premium), "", "", 1, 0)
                val c3 = ChannelTypeInfo(0, getString(R.string.adult_tag), getString(R.string.adult), "", "", 1, 0)
                val c4 = ChannelTypeInfo(0, getString(R.string.film_tag), getString(R.string.film), "", "", 1, 0)
                channelTypeList.add(c1)
                channelTypeList.add(c2)
                channelTypeList.add(c3)
                channelTypeList.add(c4)
            }
        }
        return channelTypeList
    }

    private fun initChannelType(){
        val auth = SPUtil.get(this@MainActivity, KEY_AUTHORIZATION, "") as String
        val channelTypeList = makeChannelTypeData(auth)
        val channelTypeAdapter = ChannelTypeAdapter(channelTypeList)
        rcvMain.adapter = channelTypeAdapter
        rcvMain.layoutManager = LinearLayoutManager(this@MainActivity,
                LinearLayoutManager.HORIZONTAL, false)
        channelTypeAdapter.setOnItemClickListener(object: ChannelTypeAdapter.OnItemClickListener{
            override fun onClick(view: View, position: Int) {
                val type = channelTypeList[position].tag
                val authorization: String = SPUtil.get(this@MainActivity, KEY_AUTHORIZATION, "").toString()
                if(TextUtils.isEmpty(authorization)){
                    if(TYPE_MINI != type) {
                        showAuthorizationDialog()
                        return
                    }
                }
                if(TYPE_ADULT == type){
                    handleProtect(TYPE_ADULT, position)
                }else {
                    showChannel(type, position)
                }
            }
        })
        channelTypeAdapter.setOnItemFocusListener(object: ChannelTypeAdapter.OnItemFocusListener{
            override fun onFocus(view: View, position: Int, hasFocus: Boolean) {
                presenter!!.loadAdImage()
            }
        })
        channelTypeAdapter.setOnItemLongClickListener(object : ChannelTypeAdapter.OnItemLongClickListener{
            override fun onLongClick(view: View, position: Int) {
                val type = channelTypeList[position].tag
                if(type == TYPE_ADULT){
                    val isProtect = SPUtil.get(this@MainActivity, TYPE_ADULT, true) as Boolean
                    if(!isProtect) {
                        showInputPasswordDialog(TYPE_ADULT, position)
                    }
                }

            }
        })
    }

    fun showChannel(type: String, position: Int){
        val isVoucher = SPUtil.get(KEY_IS_VOUCHER, false) as Boolean
        if(isVoucher) {
            val intent = when (position) {
                0, 1-> Intent(this@MainActivity, ChannelActivity::class.java)
                2 -> Intent(this@MainActivity, ChannelTypeActivity::class.java)
                else -> Intent("")
            }
            intent.putExtra(TYPE_CHANNEL, type)
            startActivity(intent)
        }else{
            val intent = when (position) {
                0, 1, 2-> Intent(this@MainActivity, ChannelActivity::class.java)
                3 -> Intent(this@MainActivity, ChannelTypeActivity::class.java)
                else -> Intent("")
            }
            intent.putExtra(TYPE_CHANNEL, type)
            startActivity(intent)
        }
    }

    private fun handleProtect(tag: String, position: Int) {
        val isProtect = SPUtil.get(this@MainActivity, tag, true) as Boolean
        val isSetting = SPUtil.get(this@MainActivity, tag + "protect", false) as Boolean
        val password = SPUtil.get(this@MainActivity, "protectpassword", "") as String
        if (isProtect) {
            if (TextUtils.isEmpty(password)) {
                showSettingPasswordDialog(tag)
            } else {
                if (isSetting) {
                    showInputPasswordDialog(tag, position)
                } else {
                    showSettingPasswordDialog(tag)
                }
            }
        } else {
            showChannel(tag, position)
        }
    }

    private fun showSettingPasswordDialog(tag: String) {
        val dialog = AlertDialog.Builder(this).create()
        dialog.show()
        val window = dialog.window
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager
                .LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        window.setContentView(R.layout.dialog_parent_control)
        val etP1 = window.findViewById(R.id.etP1) as EditText
        val etP2 = window.findViewById(R.id.etP2) as EditText
        val btConfirm = window.findViewById(R.id.btConfirm) as Button
        val btCancel = window.findViewById(R.id.btCancel) as Button
        btConfirm.setOnClickListener(View.OnClickListener {
            val p1 = etP1.text.toString().trim { it <= ' ' }
            val p2 = etP2.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(p1) || TextUtils.isEmpty(p2) || p1 != p2) {
                EmojiToast.show(getString(R.string.password_format_error), EmojiToast.EMOJI_SAD)
                return@OnClickListener
            }
            SPUtil.put(this@MainActivity, "protectpassword", p1)
            SPUtil.put(this@MainActivity, tag, true)
            SPUtil.put(this@MainActivity, tag + "protect", true)
            dialog.dismiss()
            EmojiToast.show(getString(R.string.password_setting_success), EmojiToast.EMOJI_SMILE)
        })
        btCancel.setOnClickListener {
            SPUtil.put(this@MainActivity, "protectpassword", "")
            SPUtil.put(this@MainActivity, tag, false)
            dialog.dismiss()
            EmojiToast.show(getString(R.string.parent_control_disabled), EmojiToast.EMOJI_SMILE)
        }
    }

    private fun showInputPasswordDialog(tag: String, position: Int) {
        val dialog = AlertDialog.Builder(this).create()
        dialog.show()
        val window = dialog.window
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager
                .LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        window.setContentView(R.layout.dialog_input_password)
        val etPassword = window.findViewById(R.id.etPassword) as EditText
        val btConfirm = window.findViewById(R.id.btConfirm) as Button
        val btReset = window.findViewById(R.id.btReset) as Button
        btConfirm.setOnClickListener(View.OnClickListener {
            val p = etPassword.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(p)) {
                EmojiToast.show(getString(R.string.password_format_error), EmojiToast.EMOJI_SAD)
                return@OnClickListener
            }
            val cp = SPUtil.get(this@MainActivity, "protectpassword", "") as String
            if (cp == p) {
                showChannel(tag, position)
                dialog.dismiss()
            } else {
                EmojiToast.show(getString(R.string.password_incorrect), EmojiToast.EMOJI_SMILE)
            }
        })
        btReset.setOnClickListener({
            showResetPasswordDialog(tag)
            dialog.dismiss()
        })
    }

    private fun showResetPasswordDialog(tag: String) {
        val dialog = AlertDialog.Builder(this).create()
        dialog.show()
        val window = dialog.window
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager
                .LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        window.setContentView(R.layout.dialog_input_password)
        val etPassword = window.findViewById(R.id.etPassword) as EditText
        val btConfirm = window.findViewById(R.id.btConfirm) as Button
        val btReset = window.findViewById(R.id.btReset) as Button
        btReset.visibility = View.GONE
        etPassword.hint = getString(R.string.notice_reset)
        btConfirm.setOnClickListener({
            val code = etPassword.text.toString().trim()
            val currentCode = SPUtil.get(this@MainActivity, KEY_AUTHORIZATION, "-1") as String
            if(currentCode == code){
                showSettingPasswordDialog(tag)
                dialog.dismiss()
            }else{
                EmojiToast.show(getString(R.string.notice_reset_error), EmojiToast.EMOJI_SAD)
            }
        })
    }

    override fun checkUpgrade(execute: Boolean, upgradeInfo: UpgradeInfo?) {
        if(execute){
            showUpgradeDialog(upgradeInfo!!)
        }else{
            if(upgradeInfo != null) {
                FileUtils.delete(getExternalFilesDir("download").absolutePath, upgradeInfo.packageName)
            }
        }
    }

    override fun loadAdImage(execute: Boolean, imagePath: String?) {
        if(execute){
//            Glide.with(this@MainActivity).load(imagePath).dontAnimate().into(ivMain)
        }
    }

    /**
     * show update dialog
     * Confirm: start update activity and start download
     * Cancel:  exit app
     */
    private fun showUpgradeDialog(upgradeInfo: UpgradeInfo){
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
            val intent = Intent(this, UpgradeActivity::class.java)
            intent.putExtra(KEY_URL, upgradeInfo.url)
            startActivity(intent)}
        btCancel.setOnClickListener { finish() }
    }

    /**
     *  Is there key active system ?
     *  YES: validate
     *  NO:  showAuthorizationDialog
     */
    private fun authorization() {
        val authorization: String = SPUtil.get(KEY_AUTHORIZATION, "").toString()
        val isVoucher: Boolean = SPUtil.get(KEY_IS_VOUCHER, false) as Boolean
//        Logger.d(authorization)
        if(TextUtils.isEmpty(authorization)){
            showAuthorizationDialog()
        }else{
            if(!isVoucher) {
                presenter!!.validateAuthorization(authorization)
            }else{
                showAuthorizationDialog()
            }
        }
    }

    private fun showAgreementDialog() {
        val agree = SPUtil.get(this@MainActivity, "agree", false) as Boolean
        if(agree) return
        val alertDialog = AlertDialog.Builder(this@MainActivity).create()
        alertDialog.show()
        alertDialog.setCancelable(false)
        val window = alertDialog.window ?: return
        window.setContentView(R.layout.dialog_update)
        val btConfirm = window.findViewById(R.id.btConfirm) as Button
        val btCancel = window.findViewById(R.id.btCancel) as Button
        val tvTitle = window.findViewById(R.id.tvTitle) as TextView
        val textView = window.findViewById(R.id.tvInfo) as TextView
        btCancel.visibility = View.GONE
        btConfirm.text = getString(R.string.agree)
        btCancel.text = getString(R.string.reject)
        tvTitle.text = getString(R.string.agreement)
        textView.textSize = 16f
        textView.text = getString(R.string.n1)
        btConfirm.setOnClickListener {
            SPUtil.put("agree", true)
            alertDialog.dismiss()
        }
        btCancel.setOnClickListener {
            SPUtil.put("agree", false)
        }
    }

    /**
     * show authorization dialog for user type int Active Key
     * Confirm: checkAuthorization
     */
    private fun showAuthorizationDialog() {

        startActivity(Intent(this, VoucherActivity::class.java))
//        val dialog = Dialog(this)
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.show()
////        dialog.setCancelable(false)
//        val window: Window = dialog.window
//        window.setContentView(R.layout.dialog_authorization)
//        val etAuthorization: EditText = window.findViewById(R.id.etAuthorization) as EditText
//        val btConfirm:Button = window.findViewById(R.id.btConfirm) as Button
//        val btVoucher:Button = window.findViewById(R.id.btVoucher) as Button
//        btConfirm.setOnClickListener {
//            val activeKey = etAuthorization.text.toString()
//            if(activeKey != TEST_ACTIVITY_KEY && (TextUtils.isEmpty(activeKey) ||
//                    activeKey.length < 16)){
//                EmojiToast.show(getString(R.string.error_key_format), EmojiToast.EMOJI_SAD)
//            }else {
//                if(!NetUtil.isConnected){
//                    EmojiToast.show(getString(R.string.no_network),
//                            EmojiToast.EMOJI_SAD)
//                    AppUtil.launchApp(this@MainActivity, PACKAGE_NAME_SETTINGS)
//                }else {
//                    presenter!!.activeAuthorization(activeKey)
//                }
//                dialog.dismiss()
//            }
//        }
//        btVoucher.setOnClickListener{
//            startActivity(Intent(this, VoucherActivity::class.java))
//            dialog.dismiss()
//        }
    }

    override fun activeAuthorization(execute: Boolean, resultInfo: ResultInfo<AuthorizationInfo>?) {
        if(!execute) return
        Logger.d(resultInfo!!)
        if(resultInfo.code == CODE_OK){
            val authorizationInfo:AuthorizationInfo = resultInfo.data!!
            EmojiToast.show(getString(R.string.active_success), EmojiToast.EMOJI_SMILE)
            SPUtil.put(KEY_AUTHORIZATION, authorizationInfo.key!!)
            SPUtil.put(KEY_LEVEL, authorizationInfo.level.toString())
            SPUtil.put(KEY_IS_VOUCHER, false)
            initChannelType()
        }else{
            EmojiToast.show(resultInfo.message!!, EmojiToast.EMOJI_SAD)
            showAuthorizationDialog()
        }
    }

    override fun validateAuthorization(execute: Boolean, resultInfo: ResultInfo<AuthorizationInfo>?) {
        if(!execute) return
        Logger.d(resultInfo!!)
        if(resultInfo.code == CODE_OK){
            val authorizationInfo:AuthorizationInfo = resultInfo.data!!
            SPUtil.put(Application.context!!, KEY_AUTHORIZATION, authorizationInfo.key!!)
            SPUtil.put(Application.context!!, KEY_LEVEL, authorizationInfo.level.toString())
            SPUtil.put(Application.context!!, KEY_TEMPORARY, authorizationInfo.temporary)
            if(authorizationInfo.level <= 0) {
                showAuthorizationDialog()
                EmojiToast.show(getString(R.string.authorization_error),
                        EmojiToast.EMOJI_SAD)
            }
        }else{
            EmojiToast.show(resultInfo.message!!, EmojiToast.EMOJI_SAD)
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
            Zoom.zoomIn10to12(v)
        }else{
            Zoom.zoomIn12to10(v)
        }
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            try {
                when (msg.what) {
                    1 -> {
                        val isVoucher = SPUtil.get(KEY_IS_VOUCHER, false) as Boolean
                        val auth = SPUtil.get(KEY_AUTHORIZATION, "") as String
                        if (isVoucher && !TextUtils.isEmpty(auth)) {
                            val expiresTime = msg.obj as String
                            val leftDays = getLeftDaysToExpires(expiresTime)
                            tvLeftTime.text = leftDays.toString() + "D"
                            if(leftDays <= 3){
                                tvLeftTime.setTextColor(Color.RED)
                                showDaysAnimate = true
                            }else{
                                tvLeftTime.setTextColor(Color.DKGRAY)
                            }
                        }
                    }
                    2-> {
                        Zoom.zoomIn01to20to10(tvLeftTime)
                    }
                }
            }catch (e: Exception){
                Logger.e(e)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if(disposable != null){
            disposable!!.dispose()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(disposable != null){
            disposable!!.dispose()
        }
    }


    private fun showTimeAndData() {
        Application.executorService!!.execute(Runnable {
            try {
                while (true) {
                    Thread.sleep(1000)
                    val expiresTime = SPUtil.get(KEY_VOUCHER_EXPIRES_TIME, "") as String
                    handler.obtainMessage(1, expiresTime).sendToTarget()
                }
            } catch (e: Exception) {
                Logger.e(e.message!!)
            }
        })
        Application.executorService!!.execute(Runnable {
            try {
                while (true) {
                    Thread.sleep(4000)
                    if(showDaysAnimate) {
                        handler.obtainMessage(2).sendToTarget()
                    }
                }
            } catch (e: Exception) {
                Logger.e(e.message!!)
            }
        })
    }




    private fun getUnixFromStr(time: String): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale("en"))
        val date: Date
        try {
            date = simpleDateFormat.parse(time)
        } catch (e: ParseException) {
            return 0
        }
        return date.time
    }

    private fun getLeftDaysToExpires(expiresTime: String): Int {
        if(TextUtils.isEmpty(expiresTime)) return 0
        val exTime = getUnixFromStr(expiresTime)
        val now = System.currentTimeMillis()
        val lTime = exTime - now
        if (lTime <= 0) {
            return 0
        }
        return (lTime / (3600000 * 24)).toInt()
    }

    private fun getLeftTimeToExpires(expiresTime: String): String {
        if(TextUtils.isEmpty(expiresTime)) return ""
        val exTime = getUnixFromStr(expiresTime)
        val now = System.currentTimeMillis()
        val lTime = exTime - now
        if (lTime <= 0) {
            return ""
        }
        var day = 0
        var hour = 0
        var minute = 0
        val second = lTime / 1000
        if (second > 60) {
            minute = (second / 60).toInt()
        }
        if (minute > 60) {
            hour = minute / 60
            minute %= 60
        }
        if (hour > 24) {
            day = hour / 24
            hour %= 24
        }
        return day.toString() + "D--" + hour + "H--" + minute + "M"
    }


}
