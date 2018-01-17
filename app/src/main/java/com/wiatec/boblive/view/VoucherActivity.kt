package com.wiatec.boblive.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.px.kotlin.utils.Logger
import com.px.kotlin.utils.SPUtil
import com.wiatec.boblive.R
import com.wiatec.boblive.adapter.VoucherCategoryAdapter
import com.wiatec.boblive.instance.KEY_AUTHORIZATION
import com.wiatec.boblive.instance.KEY_IS_VOUCHER
import com.wiatec.boblive.instance.KEY_LEVEL
import com.wiatec.boblive.pojo.ResultInfo
import com.wiatec.boblive.pojo.VoucherUserCategoryInfo
import com.wiatec.boblive.pojo.VoucherUserInfo
import com.wiatec.boblive.presenter.VoucherPresenter
import com.wiatec.boblive.utils.EmojiToast
import kotlinx.android.synthetic.main.activity_voucher.*

class VoucherActivity :  BaseActivity<IVoucher, VoucherPresenter>(), IVoucher,
        AdapterView.OnItemSelectedListener, View.OnClickListener {


    private val daysArray = arrayOf(11, 22, 33, 66, 111)
    private val priceArray = arrayOf(100F, 200F, 300F, 600F, 1000F)
    private var currentPrice = 0f
    private var currentDays = 0

    override fun createPresenter(): VoucherPresenter {
        return VoucherPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voucher)
        btActivate.setOnClickListener(this)
        initSpinner()
    }

    private fun initSpinner(){
        val planArray = arrayOf(getString(R.string.choose_plan),
                getString(R.string.plan11),
                getString(R.string.plan22),
                getString(R.string.plan33),
                getString(R.string.plan66),
                getString(R.string.plan111))
        val spPlanAdapter = ArrayAdapter<String>(this, R.layout.spinner_item, planArray)
        spPlanAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spPlan.adapter = spPlanAdapter
        spPlan.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.id){
            R.id.spPlan -> {
                if(position > 0) {
                    currentPrice = priceArray[position -1]
                    currentDays = daysArray[position -1]
                }else{
                    currentPrice = 0f
                    currentDays = 0
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btActivate -> {
                val voucherId = etVoucherId.text.toString().trim()
                if(voucherId.isEmpty()){
                    EmojiToast.show(getString(R.string.voucher_input_error), EmojiToast.EMOJI_SAD)
                    return
                }
                if(currentDays <= 0 || currentPrice <=0 ){
                    EmojiToast.show(getString(R.string.plan_choose_error), EmojiToast.EMOJI_SAD)
                    return
                }
                progressBar.visibility = View.VISIBLE
                btActivate.visibility = View.GONE
                presenter!!.activate(voucherId, currentDays.toString(), currentPrice.toString())
            }
        }
    }

    override fun onActivate(execute: Boolean, resultInfo: ResultInfo<VoucherUserInfo>?) {
        progressBar.visibility = View.GONE
        btActivate.visibility = View.VISIBLE
        if(execute){
            if(resultInfo!!.code != 200){
                EmojiToast.show(resultInfo.message!!, EmojiToast.EMOJI_SAD)
                return
            }
            EmojiToast.show(resultInfo.message!!, EmojiToast.EMOJI_SMILE)
            val voucherUserInfo = resultInfo.data
            SPUtil.put(KEY_IS_VOUCHER, true)
            SPUtil.put(KEY_LEVEL, voucherUserInfo!!.level)
            SPUtil.put(KEY_AUTHORIZATION, "voucher")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
