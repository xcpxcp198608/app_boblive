package com.wiatec.boblive.entity

/**
 * result data from API server
 */
const val CODE_OK = 200
const val CODE_CREATED = 201
const val CODE_DELETED = 204
const val CODE_INVALID = 400
const val CODE_UNAUTHORIZED = 401
const val CODE_NO_FOUND = 404
const val CODE_SERVER_ERROR = 500

data class ResultInfo<T>(var code: Int,
                         var status: String,
                         var message: String,
                         var data: ArrayList<T>)