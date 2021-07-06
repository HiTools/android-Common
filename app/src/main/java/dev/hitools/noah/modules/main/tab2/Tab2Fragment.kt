package dev.hitools.noah.modules.main.tab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FTab2Binding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class Tab2Fragment : AppBindFragment<FTab2Binding, Tab2ViewModel>() {

    override fun getLayout(): Int = R.layout.f_tab2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {

        fun newInstance(): Tab2Fragment {

            val args = Bundle()

            val fragment = Tab2Fragment()
            fragment.arguments = args
            return fragment
        }
    }
}