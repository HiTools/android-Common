package dev.hitools.noah.modules.main.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import dev.hitools.common.utils.router.AppRouter
import dev.hitools.common.utils.saver.Saver
import dev.hitools.common.widget.PrintView
import dev.hitools.common.widget.announcement.IAnnouncementData
import dev.hitools.common.widget.load.LoadSir
import dev.hitools.common.widget.load.Loader
import dev.hitools.noah.R
import dev.hitools.noah.databinding.FHomeBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindFragment
import dev.hitools.noah.ui.widget.load.AppEmptyLoad
import dev.hitools.noah.ui.widget.load.AppLoadingStatus

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    override fun getLayout(): Int = R.layout.f_home

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loader = Loader.new()
            .empty(AppEmptyLoad())
            .loading(AppLoadingStatus())
            .build()
        LoadSir.init(loader = loader)

        Saver.save("Hello", "World")
        binding.bu1.setOnClickListener {
            Log.i("yhy", "save = " + Saver.get("Hello"))
        }

    }

    override fun initViews(view: View) {
        super.initViews(view)
        PrintView.init(binding.printView)
        PrintView.reset()
    }


    @SuppressLint("SetTextI18n")
    override fun initViewModel(vm: HomeViewModel) {
        super.initViewModel(vm)
        vm.userName.observe(this, { Log.i("yhy", "userName = $it") })
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        AppRouter.with(context)
            .action("com.yuhaiyang.androidcommon.Test")
            .start()
    }

    companion object {

        fun newInstance(): HomeFragment {

            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}