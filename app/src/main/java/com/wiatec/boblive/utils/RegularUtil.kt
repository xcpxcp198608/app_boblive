package com.wiatec.boblive.utils

import java.util.regex.Pattern

/**
 * regular util
 */
object RegularUtil {

    /**
     * validate email input format
     */
    fun validateEmail(email: String): Boolean {
        val pattern = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
        return pattern.matcher(email).matches()
    }
}
