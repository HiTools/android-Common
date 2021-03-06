/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.hitools.noah.modules.sample.detail.log

import android.view.View
import dev.hitools.common.modules.log.InitLogWorker
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FragmentSampleLogBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel

/**
 * Created by Bright.Yu on 2017/2/8.
 * Log测试
 */

class SampleLogFragment : AppBindFragment<FragmentSampleLogBinding, AppBaseViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_sample_log

    fun onViewClick(v: View) {
        when (v.id) {
            R.id.request -> requestLog()
        }
    }

    private fun requestLog() {
        context?.let {
            InitLogWorker().work(it)
        }

    }

}
