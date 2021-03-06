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

package dev.hitools.noah.modules.sample.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import dev.hitools.common.extensions.showFragment
import dev.hitools.common.extensions.toast
import dev.hitools.noah.R
import dev.hitools.noah.databinding.ActivitySampleMainBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindActivity
import dev.hitools.noah.modules.sample.entries.Sample

/**
 * 测试Demo
 */
class SampleMainActivity : AppBindActivity<ActivitySampleMainBinding, SampleMainViewModel>() {
    private lateinit var viewModel: SampleMainViewModel
    private val listFragment = SampleListFragment.newInstance()
    private var lastFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.activity_sample_main)

        val fragmentManager = supportFragmentManager
        fragmentManager.addOnBackStackChangedListener {
            if (fragmentManager.backStackEntryCount == 0) {
                viewModel.updateTitle(getString(R.string.sample_main))
            }
        }
    }

    override fun initViewModel(vm: SampleMainViewModel) {
        super.initViewModel(vm)
        viewModel = vm
        showFragment(listFragment)
    }

    fun showDetail(sample: Sample) {
        val fragment = sample.action.newInstance() as? Fragment ?: return

        viewModel.updateTitle(sample.name)
        lastFragment = fragment
        showFragment(fragment, listFragment)
    }

    override fun onTitleClick(v: View) {
        super.onTitleClick(v)
        toast("title click")
    }
}
