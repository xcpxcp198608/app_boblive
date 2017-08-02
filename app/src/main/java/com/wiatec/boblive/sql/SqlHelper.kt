package com.wiatec.boblive.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by patrick on 31/07/2017.
 * create time : 3:47 PM
 */
const val DATABASE_NAME = "boblive"
const val VERSION = 1
const val TABLE_APP = "app"
const val CREATE_TABLE_APP = "create table if not exists "+ TABLE_APP+"(_id integer primary key autoincrement," +
        "name text, packageName text)"
const val DROP_TABLE_APP = "drop table if exists "+ TABLE_APP

class SqlHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null , VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_TABLE_APP)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(DROP_TABLE_APP)
        onCreate(db)
    }
}