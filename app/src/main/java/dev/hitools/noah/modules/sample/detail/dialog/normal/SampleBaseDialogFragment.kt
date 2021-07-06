package dev.hitools.noah.modules.sample.detail.dialog.normal

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import dev.hitools.common.widget.dialog.BaseDialog
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FragmentSampleBaseDialogBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment
import dev.hitools.noah.modules.base.mvvm.viewmodel.AppBaseViewModel

class SampleBaseDialogFragment : AppBindFragment<FragmentSampleBaseDialogBinding, AppBaseViewModel>() {
    override fun getLayout(): Int = R.layout.fragment_sample_base_dialog

    fun onViewClick(v: View) {
        val context = context!!
        when (v.id) {
            R.id.normal -> {
                BaseDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                    .setMessageGravity(Gravity.START or Gravity.TOP)
                    .setPositiveButton(R.string.yes, null)
                    .setNegativeButton(R.string.cancel, null)
                    .setButtonLineColor(Color.LTGRAY)
                    .setNegativeButtonTextColor(Color.GRAY)
                    .show()
            }

            R.id.noTitleDialog -> {
                BaseDialog.Builder(context)
                    .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                    .setPositiveButton(R.string.yes)
                    .setWidthProportion(0.78F)
                    .show()
            }

            R.id.listDialog -> {
                BaseDialog.Builder(context)
                    .setItems(R.array.test_array)
                    .show()
            }


            R.id.bottomDialog -> {
                BaseDialog.Builder(context, R.style.Theme_Dialog_Bottom)
                    .setTitle("提示")
                    .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                    .setNegativeButton(R.string.cancel)
                    .setMessageGravity(Gravity.START)
                    .show()
            }

            R.id.bottomDialogNoTitle -> {
                BaseDialog.Builder(context, R.style.Theme_Dialog_Bottom)
                    .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                    .setNegativeButton(R.string.cancel)
                    .setPositiveButton(R.string.yes)
                    .setMessageGravity(Gravity.START)
                    .show()
            }


        }
    }
}
