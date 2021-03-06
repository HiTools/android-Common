package dev.hitools.noah.entries.http

open class AppHttpResponse<T> {
    var code: Int = 0
    var message: String? = null
    var data: T? = null

    val isSuccess: Boolean
        get() = code == Code.Success

    companion object {

        fun empty(code: Int = Code.Failed): AppHttpResponse<*> {
            val response = AppHttpResponse<Any>()
            response.code = code
            response.message = "暂无数据"
            return response
        }

        fun exception(message: String?, code: Int = Code.Failed): AppHttpResponse<*> {
            val response = AppHttpResponse<Any>()
            response.code = code
            response.message = message
            return response
        }
    }

    object Code {
        /**
         * 返回成功
         */
        const val Success = 0

        /**
         * 返回失败
         */
        const val Failed = 1
    }


}
