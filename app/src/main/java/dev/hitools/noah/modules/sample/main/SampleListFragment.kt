package dev.hitools.noah.modules.sample.main

import android.os.Bundle
import android.view.View
import dev.hitools.common.adapter.BindAdapter
import dev.hitools.noah.BR
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FragmentSampleListBinding
import dev.hitools.noah.databinding.TestAaaBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import dev.hitools.noah.modules.sample.SampleManager
import dev.hitools.noah.modules.sample.entries.Sample

/**
 * Created by yuhaiyang on 2019-08-20.
 *
 */

class SampleListFragment : AppBindFragment<FragmentSampleListBinding, AppBaseViewModel>() {


    override fun getLayout(): Int = R.layout.fragment_sample_list


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BindAdapter<Sample>()
        adapter.setOnItemClickListener { gotoDetail(adapter.getItem(it)) }
        adapter.addLayout(BR.item, R.layout.item_sample_main)
        adapter.data = SampleManager.samples
        binding.list.adapter = adapter
    }

    private fun gotoDetail(entry: Sample) {
        (activity as SampleMainActivity).showDetail(entry)
    }

    companion object {
        fun newInstance(): SampleListFragment {
            val args = Bundle()
            val fragment = SampleListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}