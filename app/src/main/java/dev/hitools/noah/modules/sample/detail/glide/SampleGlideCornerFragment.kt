package dev.hitools.noah.modules.sample.detail.glide

import android.os.Bundle
import android.view.View
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FragmentSampleGlideCornerBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel

/**
 * Created by yuhaiyang on 2019-08-19.
 * GlideCornerSample
 */
class SampleGlideCornerFragment : AppBindFragment<FragmentSampleGlideCornerBinding, AppBaseViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_sample_glide_corner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgUrl = "https://img.i-show.club/d.jpg"
    }
}