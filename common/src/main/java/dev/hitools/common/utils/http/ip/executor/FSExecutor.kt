package dev.hitools.common.utils.http.ip.executor

import dev.hitools.common.utils.http.ip.AbsIPExecutor
import dev.hitools.common.utils.http.ip.IpUtils
import dev.hitools.common.utils.http.ip.entries.IpSource
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class FSExecutor(private val okHttpClient: OkHttpClient) : AbsIPExecutor() {

    override suspend fun execute(): IpUtils.IpInfo? {
        val request = Request.Builder()
            .url(URL)
            .get()
            .build()

        val body = requestHttp(okHttpClient, request)
        return if (body != null) {
            val responseStr = body.string()
            val info = IpUtils.IpInfo(IpSource.FeiShu, 0)
            info.ip = getIp(responseStr, "ip", "addr")
            info
        } else {
            null
        }
    }

    companion object {
        private const val URL = "https://internal-api-lark-api.feishu.cn/dns"
    }

}