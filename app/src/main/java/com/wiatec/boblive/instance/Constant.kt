package com.wiatec.boblive.instance

/**
 * constant
 */
const val URL_BASE = "http://boblive.protv.company:8080/boblive/"
const val URL_UPDATE = URL_BASE + "upgrade/"
const val URL_CHANNEL_TYPE = URL_BASE + "channel_type/"
const val URL_CHANNEL = URL_BASE + "channel/"
const val URL_AD_IMAGE = URL_BASE + "adimage/"
const val URL_ERROR_REPORT_SEND = URL_BASE + "report/send"
const val URL_ACTIVE = URL_BASE + "auth/active"
const val URL_VALIDATE = URL_BASE + "auth/validate"
const val TOKEN = "/9B67E88314F416F2092AB8ECA6A7C8EDCCE3D6D85A816E6E6F9F919B2E6C277D"

const val TYPE_LIVE = "live"
const val TYPE_MINI = "MINI"
const val TYPE_BASIC = "BASIC"
const val TYPE_PREMIUM = "PREMIUM"
const val TYPE_ADULT = "ADULT"
const val TYPE_FILMY = "FILMY"
const val TYPE_CHANNEL = "IChannelType"

const val KEY_KEY = "key"
const val KEY_MAC = "mac"
const val KEY_URL = "url"
const val KEY_LIVE = "live"
const val KEY_RELAY = "relay"
const val KEY_LEVEL = "level"
const val KEY_POSITION = "position"
const val KEY_CHANNEL_LIST = "channelInfoList"
const val KEY_EXPERIENCE = "experience"
const val KEY_TEMPORARY = "temporary"
const val KEY_LANGUAGE = "language"
const val KEY_COUNTRY = "country"
const val KEY_FIRST_BOOT = "isFirstBoot"
const val KEY_AUTHORIZATION = "authorization"

const val LANGUAGE_SK = "sk"
const val LANGUAGE_CS = "cs"

const val COUNTRY_SK = "SK"
const val COUNTRY_CS = "CZ"

const val PERCENT = "%"
const val ZERO_PERCENT = "0%"
const val HUNDRED_PERCENT = "100%"

const val PACKAGE_NAME_SETTINGS = "com.android.tv.settings"

const val TEST_ACTIVITY_KEY = "wiatec"

object Constant{

    fun adimage_path(): String = Application.context!!.getExternalFilesDir("adimages").absolutePath

}