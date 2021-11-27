package dev.hitools.common.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewbinding.ViewBinding
import dev.hitools.common.R
import dev.hitools.common.app.provider.InitProvider
import dev.hitools.common.extensions.binding.ActivityBinding
import kotlinx.coroutines.CoroutineScope

/**
 * inflate 方便实现
 * @param layoutRes layout的Id
 */
fun Activity.inflate(layoutRes: Int): View {
    return LayoutInflater.from(this).inflate(layoutRes, null)
}

/**
 * 显示Fragment
 */
fun AppCompatActivity.showFragment(fragment: Fragment, layoutId: Int = R.id.fragmentContainer) {
    val transaction = supportFragmentManager.beginTransaction()
    if (fragment.isAdded) {
        transaction.show(fragment)
    } else {
        transaction.add(layoutId, fragment)
    }
    transaction.commit()
}

/**
 * 显示Fragment
 */
@SuppressLint("WrongConstant")
fun AppCompatActivity.showFragment(
    showFragment: Fragment?,
    hideFragment: Fragment? = null,
    transitionStyle: Int = FragmentTransaction.TRANSIT_UNSET,
    layout: Int = R.id.fragmentContainer
) {
    val transaction = supportFragmentManager.beginTransaction()
    if (transitionStyle == FragmentTransaction.TRANSIT_UNSET) {
        transaction.setCustomAnimations(
            R.anim.activity_right_in,
            R.anim.activity_left_out,
            R.anim.activity_left_in,
            R.anim.activity_right_out
        )
    } else {
        transaction.setTransition(transitionStyle)
    }

    if (hideFragment != null) {
        transaction.hide(hideFragment)
    }

    if (showFragment == null) {
        transaction.commit()
        return
    }

    if (showFragment.isAdded) {
        transaction.show(showFragment)
    } else {
        transaction.add(layout, showFragment)
    }

    transaction.addToBackStack(null)
    transaction.commit()
}

/**
 * 隐藏一个Fragment
 */
fun AppCompatActivity.hideFragment(fragment: Fragment) {
    val transaction = supportFragmentManager.beginTransaction()
    transaction.hide(fragment)
    transaction.commit()
}

/**
 * 设置状态栏的颜色通过Resource 来进行设置
 */
fun AppCompatActivity.setStatusBarColorRes(@ColorRes color: Int) {
    setStatusBarColor(findColor(color))
}

/**
 * 设置状态栏的颜色
 */
fun AppCompatActivity.setStatusBarColor(@ColorInt color: Int) {
    val window = window
    window.statusBarColor = color
}

/**
 * 全屏模式 设置一个Activity 为一个全屏模式
 * @param navigationBar 是否同时隐藏 navigationBar
 */
fun AppCompatActivity.fullWindow(navigationBar: Boolean = true) {
    setStatusBarColor(Color.TRANSPARENT)

    val control = ViewCompat.getWindowInsetsController(window.decorView) ?: return
    if (navigationBar) {
        control.hide(WindowInsetsCompat.Type.systemBars())
    } else {
        control.hide(WindowInsetsCompat.Type.statusBars())
    }
}

/**
 * 是否是浅色状态栏
 */
inline val AppCompatActivity.isLightStatusBars: Boolean
    get() {
        val control = ViewCompat.getWindowInsetsController(window.decorView) ?: return false
        return control.isAppearanceLightStatusBars
    }


/**
 * 设置一个 Activity为普通模式
 */
fun AppCompatActivity.normalWindow(color: Int? = null, navigationBar: Boolean = true) {
    val statusColor = color ?: findColor(R.color.color_accent)
    setStatusBarColor(statusColor)

    val control = ViewCompat.getWindowInsetsController(window.decorView) ?: return
    if (navigationBar) {
        control.show(WindowInsetsCompat.Type.systemBars())
    } else {
        control.show(WindowInsetsCompat.Type.statusBars())
    }
}


/**
 * 方便获取ViewDataBinding
 */
inline fun <reified T : ViewBinding> AppCompatActivity.binding() = ActivityBinding(T::class.java, this)
