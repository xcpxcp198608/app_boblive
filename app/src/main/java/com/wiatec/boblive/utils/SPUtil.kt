package com.px.kotlin.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import java.security.PrivateKey

/**
 * shared preferences util
 */
object SPUtil{

    fun put(context:Context, key:String , value:Any){
        val sharedPreferences:SharedPreferences = context.getSharedPreferences("sp" , Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = sharedPreferences.edit()
        when(value){
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Float -> editor.putFloat(key, value)
            is Long -> editor.putLong(key, value)
            is Boolean -> editor.putBoolean(key, value)
            else -> editor.putString(key, value as String)
        }
        editor.apply()
    }

    fun get(context: Context, key:String, defaultValue:Any): Any{
        val sharedPreferences:SharedPreferences = context.getSharedPreferences("sp" , Context.MODE_PRIVATE)
        when(defaultValue){
            is String -> return sharedPreferences.getString(key, defaultValue)
            is Int -> return sharedPreferences.getInt(key, defaultValue)
            is Float -> return sharedPreferences.getFloat(key, defaultValue)
            is Long -> return sharedPreferences.getLong(key, defaultValue)
            is Boolean -> return sharedPreferences.getBoolean(key, defaultValue)
            else -> return ""
        }
    }
}