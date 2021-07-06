/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.hitools.noah.manager


import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import dev.hitools.common.extensions.toJSON
import dev.hitools.common.utils.StorageUtils
import dev.hitools.common.utils.StringUtils
import dev.hitools.noah.entries.UserContainer
import dev.hitools.noah.utils.http.okhttp.interceptor.AppHttpInterceptor

/**
 * Created by yuhaiyang on 2018/8/8.
 * 用户信息管理
 */
class UserManager private constructor() {

    private var userContainer: UserContainer? = null


    /**
     * 设置UserContainer
     */
    fun setUserContainer(container: UserContainer?) {
        userContainer = container
        AppHttpInterceptor.token = container?.token?.accessToken
        StorageUtils.save(UserContainer.Key.CACHE, container?.toJSON())
    }

    /**
     * 获取用户信息
     */
    fun getUserContainer(block: ((UserContainer) -> Unit)? = null): UserContainer? {
        if (userContainer == null) {
            val cache = StorageUtils.get(UserContainer.Key.CACHE, StringUtils.EMPTY)

            if (TextUtils.isEmpty(cache)) {
                Log.i(TAG, "getUser: no user")
                return null
            }
            userContainer = Gson().fromJson(cache, UserContainer::class.java)
        }

        if (userContainer != null) {
            block?.let { it(userContainer!!) }
        }

        return userContainer
    }


    /**
     * 获取头像
     */
    fun getAvatar(): String? {
        getUserContainer()
        return userContainer?.user?.avatar
    }


    /**
     * 获取AccessToken
     */
    fun getAccessToken(): String? {
        getUserContainer()
        return userContainer?.token?.accessToken
    }

    companion object {
        private const val TAG = "UserManager"

        val instance: UserManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { UserManager() }

        @JvmStatic
        fun setAvatar(avatar: String) {
            val manager = instance
            manager.getUserContainer {
                it.user?.avatar = avatar
                manager.setUserContainer(it)
            }
        }
    }

}
