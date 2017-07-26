package com.wiatec.boblive.view.custom_view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.FocusFinder
import android.view.KeyEvent
import android.view.View

/**
 * item recycle view
 */

class ItemRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                 defStyle: Int = 0)
    : RecyclerView(context, attrs, defStyle) {

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val result = super.dispatchKeyEvent(event)
        val focusView = this.findFocus()
        if (focusView == null) {
            return result
        } else {
            var dy = 0
            var dx = 0
            if (childCount > 0) {
                val firstView = this.getChildAt(0)
                dy = firstView.height
                dx = firstView.width
            }
            if (event.action == KeyEvent.ACTION_UP) {
                if (event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
                        event.keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                        event.keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
                        event.keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    return true
                } else {
                    return result
                }
            } else {
                when (event.keyCode) {
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        val rightView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_RIGHT)
                        if (rightView != null) {
                            rightView.requestFocus()
                            return true
                        } else {
                            this.smoothScrollBy(dx, 0)
                            return true
                        }
                    }
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        val leftView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_LEFT)
                        if (leftView != null) {
                            leftView.requestFocus()
                            return true
                        } else {
                            this.smoothScrollBy(-dx, 0)
                            return true
                        }
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        val downView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_DOWN)
                        if (downView != null) {
                            downView.requestFocus()
                            return true
                        } else {
                            this.smoothScrollBy(0, dy)
                            return true
                        }
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        val upView = FocusFinder.getInstance().findNextFocus(this, focusView, View.FOCUS_UP)
                        if (event.action == KeyEvent.ACTION_UP) {
                            return true
                        } else {
                            if (upView != null) {
                                upView.requestFocus()
                                return true
                            } else {
                                this.smoothScrollBy(0, -dy)
                                return true
                            }
                        }
                    }
                    else -> return result
                }
            }
        }
    }
}
