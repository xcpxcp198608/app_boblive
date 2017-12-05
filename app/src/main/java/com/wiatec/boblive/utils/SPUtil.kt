package com.px.kotlin.utils

import android.content.Context
import android.content.SharedPreferences
import com.wiatec.boblive.instance.Application

/**
 * shared preferences util
 */
object SPUtil{

    fun put(key:String , value:Any){
        put(Application.context!!, key, value)
    }

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

    fun get(key:String, defaultValue:Any): Any{
        return get(Application.context!!, key, defaultValue)
    }

    fun get(context: Context, key:String, defaultValue:Any): Any{
        val sharedPreferences:SharedPreferences = context.getSharedPreferences("sp" , Context.MODE_PRIVATE)
        return when(defaultValue){
            is String -> sharedPreferences.getString(key, defaultValue)
            is Int -> sharedPreferences.getInt(key, defaultValue)
            is Float -> sharedPreferences.getFloat(key, defaultValue)
            is Long -> sharedPreferences.getLong(key, defaultValue)
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
            else -> ""
        }
    }
}