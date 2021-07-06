package dev.hitools.common.utils.http.ip

import android.content.Context
import dev.hitools.common.app.provider.InitProvider
import dev.hitools.common.extensions.parseJSON
import dev.hitools.common.extensions.toJSON
import dev.hitools.common.utils.StorageUtils
import dev.hitools.common.utils.http.ip.entries.IpSource
import dev.hitools.common.utils.http.ip.executor.FSExecutor
import dev.hitools.common.utils.http.ip.executor.IFYExecutor
import dev.hitools.common.utils.http.ip.executor.SohuExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient


class IpUtils private constructor() {

    private var ipInfo: IpInfo? = null

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    private fun save(info: IpInfo) {
        StorageUtils.save()
            .group("ip")
            .addParam(KEY_CACHE, info.toJSON())
            .apply()
    }

    private fun getCache(context: Context?): IpInfo? {
        if (context == null) return null
        val sp = context.getSharedPreferences("ip", Context.MODE_PRIVATE)
        val cache = sp.getString(KEY_CACHE, null)
        if (cache.isNullOrEmpty()) return null
        ipInfo = cache.parseJSON()
        return ipInfo
    }

    private suspend fun start(callBack: IpCallBack? = null): IpInfo? {
        var info = FSExecutor(okHttpClient).execute()

        if (info == null) {
            info = SohuExecutor(okHttpClient).execute()
        }

        if (info == null) {
            info = IFYExecutor(okHttpClient).execute()
        }

        callBack?.invoke(info)
        info?.let {
            ipInfo = it
            save(it)
        }
        return info
    }

    companion object {
        private const val KEY_CACHE = "IP_CACHE"

        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { IpUtils() }

        fun getIp(callBack: IpCallBack? = null): IpInfo? {
            val instance = instance
            if (instance.ipInfo == null) {
                instance.getCache(InitProvider.app)
            }
            InitProvider.scope.launch(Dispatchers.IO) { instance.start(callBack) }
            return instance.ipInfo
        }
    }


    data class IpInfo(
        val source: IpSource,
        val type: Int
    ) {
        lateinit var ip: String
    }
}