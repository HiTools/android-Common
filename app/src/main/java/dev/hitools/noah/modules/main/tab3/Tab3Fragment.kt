package dev.hitools.noah.modules.main.tab3

import android.os.Bundle
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FTab3Binding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class Tab3Fragment : AppBindFragment<FTab3Binding, Tab3ViewModel>() {

    override fun getLayout(): Int = R.layout.f_tab3


    companion object {
        fun newInstance(): Tab3Fragment {

            val args = Bundle()

            val fragment = Tab3Fragment()
            fragment.arguments = args
            return fragment
        }
    }

}