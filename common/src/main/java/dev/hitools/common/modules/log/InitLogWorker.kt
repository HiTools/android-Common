package dev.hitools.common.modules.log

import android.content.Context
import android.os.Build
import dev.hitools.common.BuildConfig
import dev.hitools.common.app.provider.InitProvider
import dev.hitools.common.extensions.appName
import dev.hitools.common.extensions.appScope
import dev.hitools.common.extensions.toJSON
import dev.hitools.common.extensions.versionName
import dev.hitools.common.manager.LogManager
import dev.hitools.common.utils.DeviceUtils
import dev.hitools.common.utils.StorageUtils
import dev.hitools.common.utils.http.okhttp.interceptor.OkHttpLogInterceptor
import dev.hitools.common.utils.http.retrofit.adapter.CallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by yuhaiyang on 2019-11-01.
 * 日志工作
 */
class InitLogWorker {
    private val ss: LogManager.S by lazy {
        val logInterceptor = OkHttpLogInterceptor()
        logInterceptor.logTag = "LogWorker"

        val ok = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(logInterceptor)

        val re = Retrofit.Builder()
            .baseUrl(LogManager.S.url)
            .client(ok.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CallAdapterFactory())
            .build()
        re.create(LogManager.S::class.java)
    }

    fun work(context: Context) = appScope.launch(Dispatchers.IO){
        val screenSize = DeviceUtils.screenSize
        val app = HashMap<String, Any?>()
        app["appId"] = context.packageName
        app["appName"] = context.appName
        app["appVersion"] = context.versionName
        app["commonVersion"] = BuildConfig.VERSION
        app["commonArtifact"] = BuildConfig.ARTIFACT_ID

        val device = HashMap<String, Any?>()
        device["deviceId"] = DeviceUtils.deviceId(context)
        device["type"] = "android"
        device["manufacturer"] = Build.MANUFACTURER
        device["model"] = Build.MODEL
        device["sdk"] = Build.VERSION.SDK_INT
        device["version"] = Build.DISPLAY
        device["sw"] = context.resources.configuration.smallestScreenWidthDp
        device["resolution"] = screenSize[0].toString() + "x" + screenSize[1]

        val params = HashMap<String, Any>()
        params["app"] = app
        params["device"] = device
        params["dateTime"] = System.currentTimeMillis()
        val response = ss.init(params)
        if (response.isSuccess()) {
            StorageUtils.save(LogManager.initKey(context), response.data?.toJSON())
        }
    }
}