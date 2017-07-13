package com.wiatec.boblive

import com.wiatec.boblive.utils.AESUtil
import org.junit.Test

/**
 * Created by patrick on 16/06/2017.
 * create time : 5:25 PM
 */
class Test{

    @Test
    fun testMD5(){
        val s = AESUtil.MD5("wwww")
        System.out.println(s)
    }
}