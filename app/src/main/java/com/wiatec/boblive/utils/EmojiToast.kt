package com.wiatec.boblive.utils

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.wiatec.boblive.instance.Application

import com.wiatec.boblive.R

object EmojiToast {

    val EMOJI_SAD = 1
    val EMOJI_SMILE = 2

    fun show(message: String, emoji: Int) {
        val context = Application.context
        val toastView = LayoutInflater.from(context).inflate(R.layout.toast, null)
        val textView = toastView.findViewById(R.id.tvToast) as TextView
        textView.text = message
        var drawable: Drawable?
        if (emoji == EMOJI_SAD) {
            drawable = context!!.resources.getDrawable(R.drawable.ic_face_sad)
        } else {
            drawable = context!!.resources.getDrawable(R.drawable.ic_face_smile)
        }
        drawable!!.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
        textView.setCompoundDrawables(drawable, null, null, null)
        val toast = Toast(context)
        toast.setGravity(Gravity.BOTTOM, 0, 50)
        toast.duration = Toast.LENGTH_LONG
        toast.view = toastView
        toast.show()
    }
}
