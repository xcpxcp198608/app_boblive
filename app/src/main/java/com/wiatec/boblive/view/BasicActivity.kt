package com.wiatec.boblive.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.px.kotlin.utils.Logger
import com.wiatec.boblive.R
import com.wiatec.boblive.adapter.AppAdapter
import com.wiatec.boblive.presenter.CommonPresenter
import com.wiatec.boblive.sql.AppDao
import com.wiatec.boblive.utils.AppUtil
import kotlinx.android.synthetic.main.activity_basic.*

/**
 * Created by patrick on 31/07/2017.
 * create time : 9:36 AM
 */
class BasicActivity: BaseActivity<ICommon, CommonPresenter>(), ICommon {

    override fun createPresenter(): CommonPresenter {
        return CommonPresenter(this@BasicActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)
        loadApp()
    }

    private fun loadApp() {
        val appList = AppDao().queryAll()
        val appAdapter = AppAdapter(appList)
        rcvBasic.adapter = appAdapter
        rcvBasic.layoutManager = GridLayoutManager(this@BasicActivity, 6)
        appAdapter.setOnItemClickListener(object: AppAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                AppUtil.launchApp(this@BasicActivity, appList[position].packageName)
            }
        })
    }

    override fun loadAdImage(execute: Boolean, imagePath: String?) {
    }
}