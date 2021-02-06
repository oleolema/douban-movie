package com.yqh.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class ApiResult<T>(
    val status: Int,
    val message: String,
    private val _data: T?
) {

    /** 相当于 success */
    constructor() : this(200, "success", null)

    override fun toString(): String {
        return Gson().toJson(this)
    }

    val data: T
        get() = _data ?: Gson().fromJson("", object : TypeToken<T>() {}.type)

    companion object {
        @JvmStatic
        fun getInstance(status: Int, message: String): ApiResult<String> {
            return ApiResult(status, message, message)
        }

        @JvmStatic
        fun success(): ApiResult<String> {
            return success("success")
        }

        @JvmStatic
        fun success(data: List<*>, count: Long, page: Int, pageSize: Int): ApiResult<Map<String, Any>> {
            return ApiResult(
                200, "success",
                mapOf("data" to data, "count" to count, "page" to page, "pageSize" to pageSize)
            )
        }

        @JvmStatic
        fun <T> success(data: T): ApiResult<T> {
            return ApiResult(200, "success", data)
        }

        @JvmStatic
        fun <T> fail(message: String?): ApiResult<T> {
            return ApiResult(
                400, message
                    ?: "未知错误", null
            )
        }

        @JvmStatic
        fun <T> fail(e: Exception): ApiResult<T> {
            return ApiResult(
                400, e.message
                    ?: "未知错误", null
            )
        }

        @JvmStatic
        fun <T> forbidden(): ApiResult<T> {
            return forbidden(null)
        }

        @JvmStatic
        fun <T> forbidden(message: String?): ApiResult<T> {
            return ApiResult(
                400,
                message ?: "该用户没有该资源权限",
                null
            )
        }
    }
}
