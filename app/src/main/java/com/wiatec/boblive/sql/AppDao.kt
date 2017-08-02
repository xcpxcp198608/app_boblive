package com.wiatec.boblive.sql

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.wiatec.boblive.Application
import com.wiatec.boblive.pojo.AppInfo

/**
 * Created by patrick on 31/07/2017.
 * create time : 3:53 PM
 */
class AppDao {

    var sql: SQLiteDatabase? = null

    init {
        sql = SqlHelper(Application.context!!).writableDatabase
    }

    fun exists(appInfo: AppInfo): Boolean {
        val cursor: Cursor = sql!!.query(TABLE_APP, null, "packageName=?",
                arrayOf(appInfo.packageName), null, null, null)
        val flag = cursor.moveToNext()
        cursor.close()
        return flag
    }

    fun insert(appInfo: AppInfo){
        val contentValues: ContentValues = ContentValues()
        contentValues.put("name", appInfo.name)
        contentValues.put("packageName", appInfo.packageName)
        sql!!.insert(TABLE_APP, null, contentValues)
    }

    fun update(appInfo: AppInfo){
        val contentValues: ContentValues = ContentValues()
        contentValues.put("name", appInfo.name)
        sql!!.update(TABLE_APP, contentValues, "packageName=?", arrayOf(appInfo.packageName))
    }

    fun insertOrUpdate(appInfo: AppInfo){
        if(exists(appInfo)){
            update(appInfo)
        }else{
            insert(appInfo)
        }
    }

    fun delete(appInfo: AppInfo){
        sql!!.delete(TABLE_APP, "packageName=?", arrayOf(appInfo.packageName))
    }

    fun queryAll(): ArrayList<AppInfo> {
        val cursor: Cursor = sql!!.query(TABLE_APP, null, "_id>?", arrayOf("0"), null, null, "name")
        val appList = ArrayList<AppInfo>()
        while (cursor.moveToNext()){
            val appInfo = AppInfo()
            appInfo.name = cursor.getString(cursor.getColumnIndex("name"))
            appInfo.packageName = cursor.getString(cursor.getColumnIndex("packageName"))
            appList.add(appInfo)
        }
        cursor.close()
        return appList
    }
}