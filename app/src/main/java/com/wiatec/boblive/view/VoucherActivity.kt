package com.wiatec.boblive.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

    private val monthArray = arrayOf("choose month", "1", "2", "3", "6", "12", "24", "36")
    private var currentPrice = 0f
    private var currentMonth = 0
    private var currentAmount = 0f
    private var currentCategory = ""
    private var voucherUserCategoryInfoList: ArrayList<VoucherUserCategoryInfo>? = null

    override fun createPresenter(): VoucherPresenter {
        return VoucherPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voucher)
        btActivate.setOnClickListener(this)
        presenter!!.getCategory()
    }

    override fun onCategory(execute: Boolean, resultInfo: ResultInfo<VoucherUserCategoryInfo>?) {
        if(execute){
            if(resultInfo!!.code != 200){
                return
            }
            val list: ArrayList<VoucherUserCategoryInfo> = resultInfo.dataList as ArrayList<VoucherUserCategoryInfo>
            if(list.size <= 0) return
            voucherUserCategoryInfoList = list
            val voucherCategoryAdapter = VoucherCategoryAdapter(list)
            rcvCategory.adapter = voucherCategoryAdapter
            rcvCategory.layoutManager = GridLayoutManager(this, list.size)

            val planArray = arrayOfNulls<String>(list.size + 1)
            planArray[0] = "choose plan"
            for ((index, value) in list.withIndex()){
                planArray[index + 1] = value.category
            }
            val spPlanAdapter = ArrayAdapter<String>(this, R.layout.spinner_item, planArray)
            spPlanAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spPlan.adapter = spPlanAdapter
            val spMonthAdapter = ArrayAdapter<String>(this, R.layout.spinner_item, monthArray)
            spMonthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spMonth.adapter = spMonthAdapter
            spPlan.onItemSelectedListener = this
            spMonth.onItemSelectedListener = this
            spPlan.requestFocus()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    @SuppressLint("SetTextI18n")
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent!!.id){
            R.id.spPlan -> {
                if(position > 0) {
                    currentPrice = voucherUserCategoryInfoList!![position -1].price
                    currentCategory = voucherUserCategoryInfoList!![position -1].category!!
                }else{
                    currentPrice = 0f
                    currentCategory = ""
                }
            }
            R.id.spMonth -> {
                currentMonth = if(position > 0) {
                    Integer.parseInt(monthArray[position])
                }else{
                    0
                }
            }
        }
        currentAmount = currentMonth * currentPrice
        tvAmount.text = getString(R.string.total_amount) + currentAmount
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btActivate -> {
                val voucherId = etVoucherId.text.toString().trim()
                if(voucherId.isEmpty()){
                    EmojiToast.show("voucher id input error", EmojiToast.EMOJI_SAD)
                    return
                }
                if(currentMonth <= 0){
                    EmojiToast.show("month choose error", EmojiToast.EMOJI_SAD)
                    return
                }
                if(currentCategory.isEmpty()){
                    EmojiToast.show("plan choose error", EmojiToast.EMOJI_SAD)
                    return
                }
                progressBar.visibility = View.VISIBLE
                btActivate.visibility = View.GONE
                presenter!!.activate(voucherId, currentCategory, currentMonth.toString())
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
