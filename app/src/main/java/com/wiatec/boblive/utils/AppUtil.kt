package com.wiatec.boblive.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import java.io.File

/**
 * Created by patrick on 17/06/2017.
 * create time : 9:55 AM
 */
object AppUtil{

    /**
     * check app already installed or not
     */
    fun isInstalled(context: Context, packageName: String): Boolean {
        if (TextUtils.isEmpty(packageName)) {
            return false
        }
        try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("----px----", e.message)
            return false
        }
    }

    /**
     * get app icon by package name
     */
    fun getIcon(context: Context, packageName: String): Drawable? {
        val packageManager = context.packageManager
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (applicationInfo != null) {
            return applicationInfo.loadIcon(packageManager)
        } else {
            return null
        }
    }

    /**
     * get app name by package name
     */
    fun getLabelName(context: Context, packageName: String): String? {
        val packageManager = context.packageManager
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        if (applicationInfo != null) {
            return applicationInfo.loadLabel(packageManager).toString()
        } else {
            return null
        }
    }

    /**
     * get app version by package name
     */
    fun getVersionName(context: Context, packageName: String): String {
        if (TextUtils.isEmpty(packageName)) {
            return "apkPackageName error"
        }
        var apkVersionName: String? = null
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null) {
                apkVersionName = packageInfo.versionName
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            //            Log.d("----px----" , e.getMessage());
        }
        if(apkVersionName != null) {
            return apkVersionName
        }else{
            return  ""
        }
    }

    /**
     * get app version code by package name
     */
    fun getVersionCode(context: Context, packageName: String): Int {
        if (TextUtils.isEmpty(packageName)) {
            return 0
        }
        var apkVersionCode = 0
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null) {
                apkVersionCode = packageInfo.versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            //Log.d("----px----" , e.getMessage());
        }
        //Log.d("----px----" , apkVersionCode+"");
        return apkVersionCode
    }

    /**
     * check current version is last version or not
     */
    fun isNeedUpdate(context: Context, versionCode: Int): Boolean {
        if (isInstalled(context, context.packageName)) {
            val localVersionCode = getVersionCode(context, context.packageName)
            return localVersionCode < versionCode
        } else {
            return false
        }
    }

    /**
     * get package name by apk file
     */
    fun getApkPackageName(context: Context, filePath: String, fileName: String): String? {
        val applicationInfo: ApplicationInfo?
        val packageInfo: PackageInfo?
        val packageManager = context.packageManager
        val apkPackageName: String?
        packageInfo = packageManager.getPackageArchiveInfo(filePath + fileName, PackageManager.GET_ACTIVITIES)
        if (packageInfo != null) {
            applicationInfo = packageInfo.applicationInfo
            apkPackageName = applicationInfo!!.packageName
        } else {
            return null
        }
        //Log.d("----px----" ,apkPackageName);
        return apkPackageName
    }

    /**
     * get app version nam by app file
     */
    fun getApkVersionName(context: Context, filePath: String, fileName: String): String? {
        val packageInfo: PackageInfo?
        val packageManager = context.packageManager
        val apkVersionName: String?
        packageInfo = packageManager.getPackageArchiveInfo(filePath + fileName, PackageManager.GET_ACTIVITIES)
        if (packageInfo != null) {
            apkVersionName = packageInfo.versionName
        } else {
            return null
        }
//        Log.d("----px----", apkVersionName)
        return apkVersionName
    }

    /**
     * get apk file version code
     */
    fun getApkVersionCode(context: Context, filePath: String, fileName: String): Int {
        val packageInfo: PackageInfo?
        val packageManager = context.packageManager
        packageInfo = packageManager.getPackageArchiveInfo(filePath + fileName, PackageManager.GET_ACTIVITIES)
        if (packageInfo != null) {
            return packageInfo.versionCode
        } else {
            return 0
        }
    }

    /**
     * check apk file is can install
     */
    fun isApkCanInstall(context: Context, filePath: String, fileName: String): Boolean {
        var packageInfo: PackageInfo?
        val packageManager = context.packageManager
        packageInfo = packageManager.getPackageArchiveInfo(filePath + "/" + fileName, PackageManager.GET_ACTIVITIES)
        return packageInfo != null
    }

    /**
     * install apk file
     */
    fun installApk(context: Context, filePath: String, fileName: String) {
        val file = File(filePath, fileName)
        if (!file.exists()) {
            Toast.makeText(context, "Apk file is not exists", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isApkCanInstall(context, filePath, fileName)) {
            Toast.makeText(context, "Apk file can not install", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        context.startActivity(intent)
    }

    /**
     * launch already installed apk by package name
     */
    fun launchApp(context: Context, packageName: String) {
        if (!isInstalled(context, packageName)) {
            return
        }
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            context.startActivity(intent)
        }
    }
}