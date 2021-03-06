package dev.hitools.noah.data.retrofit

import dev.hitools.noah.BuildConfig
import dev.hitools.noah.entries.UserContainer
import dev.hitools.noah.entries.http.AppHttpResponse
import dev.hitools.noah.entries.http.AppPageResponse
import dev.hitools.noah.entries.params.request.ForgotPasswordParams
import dev.hitools.noah.entries.params.request.LoginParams
import dev.hitools.noah.entries.params.request.RegisterParams
import dev.hitools.noah.modules.sample.entries.SampleTestPage
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    companion object {
        val BASE_URL: String
            get() =
                when (BuildConfig.VERSION_TYPE) {
                    BuildConfig.VERSION_DEV,
                    BuildConfig.VERSION_SIT,
                    BuildConfig.VERSION_UAT,
                    BuildConfig.VERSION_PROD -> "https://api.yuhaiyang.net/common/"
                    else -> "https://api.yuhaiyang.net/common/"
                }
    }

    /**
     * 登录
     */
    @POST("account/login")
    fun login(@Body params: LoginParams): AppHttpResponse<UserContainer>

    /**
     * 通过Token来处理
     */
    @POST("account/loginByToken")
    fun loginByToken(): AppHttpResponse<UserContainer>

    /**
     * 用户注册
     */
    @POST("account/register")
    fun register(@Body params: RegisterParams): AppHttpResponse<UserContainer>

    /**
     * 忘记密码
     */
    @POST("account/forgotPassword")
    fun forgotPassword(@Body params: ForgotPasswordParams): AppHttpResponse<Any>

    /**
     *
     */
    @Multipart
    @POST("account/uploadAvatar")
    // fun uploadAvatar(@Part("file\"; filename=\"image.jpg") file: RequestBody): AppHttpResponse<String>
    fun uploadAvatar(@Part file:  MultipartBody.Part): AppHttpResponse<String>

    /**
     * TestPage
     */
    @GET("test/page")
    fun testPage(@Query("page") page: Int, @Query("pageSize") pageSize: Int): AppPageResponse<SampleTestPage>
}