package dev.hitools.noah.modules.sample.detail.recycle.adapter

import android.os.Bundle
import android.view.View
import dev.hitools.common.adapter.BindAdapter
import dev.hitools.common.extensions.toast
import dev.hitools.noah.BR
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FSampleBindAdapterBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment
import dev.hitools.noah.modules.sample.entries.Sample

/**
 * Created by yuhaiyang on 2020-02-14.
 */
class SampleBindAdapterFragment : AppBindFragment<FSampleBindAdapterBinding, SampleBindAdapterViewModel>() {

    val adapter = BindAdapter<Sample>()

    override fun getLayout(): Int = R.layout.f_sample_bind_adapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.addLayout(BR.item, R.layout.item_sample_bind_adapter)
        adapter.setOnItemClickListener {
            toast("点击了 $it")
        }

        adapter.setOnItemChildClickListener(R.id.button) { position, viewId ->
            toast("点击了 $position, id为：$viewId")
        }
        binding.list.adapter = adapter

        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))
        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))
        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))
        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))
        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))
        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))


    }
}