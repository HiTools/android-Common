package dev.hitools.noah.modules.sample.detail.androidx.coordinatorLayout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FSampleCoordinatorLayoutBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment

/**
 * Created by yuhaiyang on 2020-06-29.
 */
class SampleCoordinatorLayoutFragment : AppBindFragment<FSampleCoordinatorLayoutBinding, SampleCoordinatorLayoutViewModel>() {

    override fun getLayout(): Int = R.layout.f_sample_coordinator_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.appBar.addOnOffsetChangedListener(TopEffectListener(R.id.toolbar))
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(binding.toolbar)
        activity.supportActionBar?.setHomeButtonEnabled(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}