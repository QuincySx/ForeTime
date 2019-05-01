package com.smallraw.foretime.app.repository.http.response

/**
 * @author QuincySx
 * @date 2018/7/19 上午10:46
 */
open class BaseResponse<T> {
    var error: Int = 0
    var message: String? = null
    var data: T? = null
}
