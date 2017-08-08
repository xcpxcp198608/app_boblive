package com.wiatec.boblive.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide

import com.wiatec.boblive.R
import com.wiatec.boblive.presenter.AdPresenter
import kotlinx.android.synthetic.main.activity_ad.*

class AdActivity : BaseActivity<ICommon, AdPresenter>(), ICommon {

    override fun createPresenter(): AdPresenter {
        return AdPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ad)
    }


    override fun loadAdImage(execute: Boolean, imagePath: String?) {
        if(execute){
            Glide.with(this@AdActivity).load(imagePath).into(ivAd)
        }
    }
}
