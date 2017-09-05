package com.wiatec.boblive.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

object Zoom {

    fun zoomIn09to10(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1.0f)
        val animator1 = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1.0f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 150
        animatorSet.play(animator).with(animator1)
        animatorSet.start()
    }

    fun zoomIn10to11(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.1f)
        val animator1 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.1f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 150
        animatorSet.play(animator).with(animator1)
        animatorSet.start()
    }

    fun zoomIn11to10(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1.0f)
        val animator1 = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1.0f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 150
        animatorSet.play(animator).with(animator1)
        animatorSet.start()
    }

    fun zoomIn10to12(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.2f)
        val animator1 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.2f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 150
        animatorSet.play(animator).with(animator1)
        animatorSet.start()
    }

    fun zoomIn12to10(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1.0f)
        val animator1 = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1.0f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 150
        animatorSet.play(animator).with(animator1)
        animatorSet.start()
    }

    fun zoomIn10to13(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.3f)
        val animator1 = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.3f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 150
        animatorSet.play(animator).with(animator1)
        animatorSet.start()
    }

    fun zoomIn13to10(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "scaleX", 1.3f, 1.0f)
        val animator1 = ObjectAnimator.ofFloat(view, "scaleY", 1.3f, 1.0f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 150
        animatorSet.play(animator).with(animator1)
        animatorSet.start()
    }
}
