package dev.hitools.noah.modules.main.index

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.hitools.common.utils.ToastUtils
import dev.hitools.common.widget.BottomBar
import dev.hitools.noah.R
import dev.hitools.noah.databinding.AMainBinding
import dev.hitools.noah.modules.base.mvvm.view.AppBindActivity
import dev.hitools.noah.modules.main.home.HomeFragment
import dev.hitools.noah.modules.main.mine.MineFragment
import dev.hitools.noah.modules.main.tab2.Tab2Fragment
import dev.hitools.noah.modules.main.tab3.Tab3Fragment

/**
 * Created by yuhaiyang on 2021-01-15.
 */
class MainActivity : AppBindActivity<AMainBinding, MainViewModel>(), BottomBar.OnBottomBarListener  {

    private lateinit var tab1Fragment: HomeFragment
    private lateinit var tab2Fragment: Tab2Fragment
    private lateinit var tab3Fragment: Tab3Fragment
    private lateinit var tab4Fragment: MineFragment
    private val fragmentList = mutableListOf<Fragment>()
    private var lastTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.a_main)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val type = intent.getIntExtra(KEY_TYPE, TAB_FIRST)
        binding.bottomBar.setSelectedId(type, true)
    }

    override fun initViews() {
        super.initViews()
        tab1Fragment = HomeFragment.newInstance()
        fragmentList.add(tab1Fragment)
        tab2Fragment = Tab2Fragment.newInstance()
        fragmentList.add(tab2Fragment)
        tab3Fragment = Tab3Fragment.newInstance()
        fragmentList.add(tab3Fragment)
        tab4Fragment = MineFragment.newInstance()
        fragmentList.add(tab4Fragment)

        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragmentList.size

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }

        binding.bottomBar.setOnSelectedChangedListener(this)
    }

    override fun onBackPressed() {
        val nowTime = System.currentTimeMillis()
        if (nowTime - lastTime < 2000) {
            super.onBackPressed()
        } else {
            lastTime = nowTime
            ToastUtils.show(this, R.string.click_again_to_exit)
        }
    }

    override fun onSelectedChanged(parent: ViewGroup, @IdRes selectId: Int, index: Int) {
        binding.viewPager.setCurrentItem(index, false)
    }

    companion object {
        const val TAB_FIRST = R.id.tab_1
    }
}