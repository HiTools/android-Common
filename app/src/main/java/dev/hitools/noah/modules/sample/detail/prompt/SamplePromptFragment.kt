package dev.hitools.noah.modules.sample.detail.prompt

import dev.hitools.noah.R
import dev.hitools.noah.databinding.FragmentSamplePermissionBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel

/**
 * 关于提示信息类别的测试类
 */
class SamplePromptFragment : AppBindFragment<FragmentSamplePermissionBinding, AppBaseViewModel>() {

    override fun getLayout(): Int {
        return R.layout.fragement_sample_prompt
    }
}
