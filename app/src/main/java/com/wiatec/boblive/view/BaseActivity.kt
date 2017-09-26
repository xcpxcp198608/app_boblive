package com.wiatec.boblive.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.instance.Application
import com.wiatec.boblive.instance.KEY_AUTHORIZATION
import com.wiatec.boblive.R

import com.wiatec.boblive.presenter.BasePresenter
import com.wiatec.boblive.rxevent.ValidateEvent
import com.wiatec.boblive.utils.RxBus
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers

/**
 * Created by patrick on 17/06/2017.
 * create time : 11:07 AM
 */

abstract class BaseActivity<V, T : BasePresenter<V>> : AppCompatActivity() {

    abstract fun createPresenter(): T
    var presenter: T? = null
    private var validateSubscription:Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
        if(presenter != null) {
            presenter!!.attach(this as V)
        }
    }

    protected fun checkValidate(context: Context){
        validateSubscription = RxBus.default!!.toObservable(ValidateEvent::class.java)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ (message) ->
                    showValidateErrorDialog(context, message)
                    validateSubscription!!.unsubscribe()
                }
    }

    private fun  showValidateErrorDialog(context: Context, message: String) {
        val dialog: Dialog = AlertDialog.Builder(context).create()
        dialog.show()
        dialog.setCancelable(false)
        val window: Window = dialog.window
        window.setContentView(R.layout.dialog_update)
        val tvInfo: TextView = window.findViewById(R.id.tvInfo) as TextView
        val btConfirm: Button = window.findViewById(R.id.btConfirm) as Button
        tvInfo.text = getString(R.string.authorization_error) + "(" + message + ")"
        btConfirm.requestFocus()
        btConfirm.setOnClickListener {
            dialog.dismiss()
            SPUtil.put(Application.context!!, KEY_AUTHORIZATION, "")
            startActivity(Intent(Application.context!!, MainActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(presenter != null) {
            presenter!!.detach()
        }
        if(validateSubscription != null){
            validateSubscription!!.unsubscribe()
        }
    }
}
