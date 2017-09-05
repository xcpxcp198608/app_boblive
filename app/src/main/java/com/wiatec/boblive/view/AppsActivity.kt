package com.wiatec.boblive.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.wiatec.boblive.R
import com.wiatec.boblive.adapter.AppAdapter
import com.wiatec.boblive.presenter.CommonPresenter
import com.wiatec.boblive.sql.AppDao
import com.wiatec.boblive.utils.AppUtil
import kotlinx.android.synthetic.main.activity_apps.*

/**
 * Created by patrick on 31/07/2017.
 * create time : 9:36 AM
 */
class AppsActivity : BaseActivity<ICommon, CommonPresenter>(), ICommon {

    override fun createPresenter(): CommonPresenter {
        return CommonPresenter(this@AppsActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps)
    }

    override fun onStart() {
        super.onStart()
        checkValidate(this)
        loadApp()
    }

    private fun loadApp() {
        val appList = AppDao().queryAll()
        val appAdapter = AppAdapter(appList)
        rcvApps.adapter = appAdapter
        rcvApps.layoutManager = GridLayoutManager(this@AppsActivity, 6)
        appAdapter.setOnItemClickListener(object: AppAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                AppUtil.launchApp(this@AppsActivity, appList[position].packageName)
            }
        })
    }

    override fun loadAdImage(execute: Boolean, imagePath: String?) {
    }
}