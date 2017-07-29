package com.wiatec.boblive

/**
 * constant
 */
const val URL_UPDATE = "http://www.ldlegacy.com:8080/boblive/upgrade/"
const val URL_CHANNEL_TYPE = "http://www.ldlegacy.com:8080/boblive/channel_type"
const val URL_CHANNEL = "http://www.ldlegacy.com:8080/boblive/channel/"
const val URL_AD_IMAGE = "http://www.ldlegacy.com:8080/boblive/adimage/"
const val TOKEN = "/9B67E88314F416F2092AB8ECA6A7C8EDCCE3D6D85A816E6E6F9F919B2E6C277D"

const val URL_ACTIVE = "http://www.ldlegacy.com:8080/boblive/auth/active"
const val URL_VALIDATE = "http://www.ldlegacy.com:8080/boblive/auth/validate"

const val TYPE_LIVE = "live"
const val TYPE_APP = "app"
const val TYPE_RADIO = "radio"
const val TYPE_CHANNEL = "channelType"

const val KEY_KEY = "key"
const val KEY_MAC = "mac"
const val KEY_URL = "url"
const val KEY_LEVEL = "level"
const val KEY_POSITION = "position"
const val KEY_CHANNEL_LIST = "channelInfoList"
const val KEY_EXPERIENCE = "experience"
const val KEY_LANGUAGE = "language"
const val KEY_FIRST_BOOT = "isFirstBoot"
const val KEY_AUTHORIZATION = "authorization"

const val LANGUAGE_SK = "sk"
const val LANGUAGE_CS = "cs"

const val COUNTRY_SK = "SK"
const val COUNTRY_CS = "CZ"

const val PERCENT = "%"
const val ZERO_PERCENT = "0%"
const val HUNDRED_PERCENT = "100%"

object Constant{

    fun image_path(): String{
        return Application.context!!.getExternalFilesDir("adimages").absolutePath
    }
}