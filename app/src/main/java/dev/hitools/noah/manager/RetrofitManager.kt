package dev.hitools.noah.manager

import dev.hitools.common.utils.http.okhttp.interceptor.OkHttpLogInterceptor
import dev.hitools.noah.data.retrofit.ApiService
import dev.hitools.noah.data.retrofit.LogService
import dev.hitools.noah.utils.http.okhttp.interceptor.AppHttpInterceptor
import dev.hitools.noah.utils.http.retrofit.adapter.AppCallAdapterFactory
import dev.hitools.noah.utils.http.retrofit.convert.AppConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class RetrofitManager private constructor() {

    val appService: ApiService by lazy {

        val okBuilder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(AppHttpInterceptor())
            .addInterceptor(OkHttpLogInterceptor())

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(okBuilder.build())
            .addConverterFactory(AppConverterFactory.create())
            .addCallAdapterFactory(AppCallAdapterFactory())
            .build()

        retrofit.create(ApiService::class.java)
    }

    @Suppress("ConstantConditionIf")
    val logService: LogService by lazy {
        val okBuilder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(AppHttpInterceptor())
            .addInterceptor(OkHttpLogInterceptor())

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.baidu.com/")
            .client(okBuilder.build())
            .addConverterFactory(AppConverterFactory.create())
            .addCallAdapterFactory(AppCallAdapterFactory())
            .build()

        retrofit.create(LogService::class.java)
    }

    companion object {

        @Volatile
        private var sInstance: RetrofitManager? = null

        val instance: RetrofitManager
            get() =
                sInstance ?: synchronized(RetrofitManager::class.java) {
                    sInstance ?: RetrofitManager().also { sInstance = it }
                }
    }
}