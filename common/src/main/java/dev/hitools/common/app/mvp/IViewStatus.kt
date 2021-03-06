/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.hitools.common.app.mvp

import dev.hitools.common.entries.status.Empty
import dev.hitools.common.entries.status.Error
import dev.hitools.common.entries.status.Loading
import dev.hitools.common.entries.status.Success

/**
 * Created by yuhaiyang on 2019/6/25.
 * View状态的更新
 */

interface IViewStatus {
    /**
     * 显示加载动画
     */
    fun showLoading(loading: Loading)

    /**
     * 隐藏加载动画
     */
    fun dismissLoading(loading: Loading)

    /**
     * 加载失败
     */
    fun showError(error: Error)

    /**
     * 加载成功
     */
    fun showSuccess(success: Success)

    /**
     * 展示空数据
     */
    fun showEmpty(empty: Empty)
}
