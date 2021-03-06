package dev.hitools.common.widget.load

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import dev.hitools.common.app.provider.InitProvider
import dev.hitools.common.widget.load.status.*
import dev.hitools.common.widget.load.target.ITarget
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Created by yuhaiyang on 2020/9/15.
 * 加载的工具
 */
class Loader internal constructor() : Serializable {
    private lateinit var loadLayout: LoadLayout
    private lateinit var target: ITarget

    private var typeMap = hashMapOf<Type, ALoadStatus>()

    private var emptyText: HashMap<Int, String> = hashMapOf()
    private var emptyIcon: HashMap<Int, Int> = hashMapOf()

    private var errorText: HashMap<Int, String> = hashMapOf()
    private var errorIcon: HashMap<Int, Int> = hashMapOf()

    private var loadingText: HashMap<Int, String> = hashMapOf()
    private var loadingIcon: HashMap<Int, Int> = hashMapOf()

    private var successText: HashMap<Int, String> = hashMapOf()
    private var successIcon: HashMap<Int, Int> = hashMapOf()

    private var margin: IntArray = intArrayOf(0, 0, 0, 0)

    private var block: OnLoaderClickBlock? = null

    /**
     * 展示状态
     */
    fun show(type: Type) = when (type) {
        Type.Empty -> showEmpty()
        Type.Loading -> showLoading()
        Type.Error -> showError()
        Type.Success -> showSuccess()
        Type.Custom -> {
        }
    }

    fun showEmpty() {
        val status = typeMap[Type.Empty]
        status?.block = block

        loadLayout.showStatus(typeMap[Type.Empty], margin) { view ->
            emptyText.forEach { (key, value) -> setText(view, key, value) }
            emptyIcon.forEach { (key, value) -> setImage(view, key, value) }
        }
    }

    fun showLoading() {
        loadLayout.showStatus(typeMap[Type.Loading], margin) { view ->
            loadingText.forEach { (key, value) -> setText(view, key, value) }
            loadingIcon.forEach { (key, value) -> setImage(view, key, value) }
        }
    }

    fun showError() {
        loadLayout.showStatus(typeMap[Type.Error], margin) { view ->
            errorText.forEach { (key, value) -> setText(view, key, value) }
            errorIcon.forEach { (key, value) -> setImage(view, key, value) }
        }
    }

    fun showSuccess() {
        loadLayout.showStatus(typeMap[Type.Success], margin) { view ->
            successText.forEach { (key, value) -> setText(view, key, value) }
            successIcon.forEach { (key, value) -> setImage(view, key, value) }
        }
    }

    /**
     * 隐藏
     */
    fun dismiss() {
        loadLayout.dismiss()
    }


    internal fun setTarget(target: ITarget) {
        this.target = target
        loadLayout = target.replace(this)
    }

    fun emptyText(@IdRes viewId: Int, @StringRes resId: Int): Loader {
        val context = InitProvider.app
        emptyText[viewId] = context.getString(resId)
        return this
    }

    fun emptyText(@IdRes viewId: Int, text: String): Loader {
        emptyText[viewId] = text
        return this
    }

    fun emptyImage(@IdRes viewId: Int, @DrawableRes resId: Int): Loader {
        emptyIcon[viewId] = resId
        return this
    }

    fun errorText(@IdRes viewId: Int, @StringRes resId: Int): Loader {
        val context = InitProvider.app
        errorText[viewId] = context.getString(resId)
        return this
    }

    fun errorText(@IdRes viewId: Int, text: String): Loader {
        errorText[viewId] = text
        return this
    }

    fun errorImage(@IdRes viewId: Int, @DrawableRes resId: Int): Loader {
        errorIcon[viewId] = resId
        return this
    }

    fun successText(@IdRes viewId: Int, @StringRes resId: Int): Loader {
        val context = InitProvider.app
        successText[viewId] = context.getString(resId)
        return this
    }

    fun successText(@IdRes viewId: Int, text: String): Loader {
        successText[viewId] = text
        return this
    }

    fun successImage(@IdRes viewId: Int, @DrawableRes resId: Int): Loader {
        successIcon[viewId] = resId
        return this
    }

    fun loadingText(@IdRes viewId: Int, @StringRes resId: Int): Loader {
        val context = InitProvider.app
        loadingText[viewId] = context.getString(resId)
        return this
    }

    fun loadingText(@IdRes viewId: Int, text: String): Loader {
        loadingText[viewId] = text
        return this
    }

    fun loadingImage(@IdRes viewId: Int, @DrawableRes resId: Int): Loader {
        loadingIcon[viewId] = resId
        return this
    }

    fun marginStart(size: Int): Loader {
        margin[0] = size
        return this
    }

    fun marginTop(size: Int): Loader {
        margin[1] = size
        return this
    }

    fun marginEnd(size: Int): Loader {
        margin[2] = size
        return this
    }

    fun marginBottom(size: Int): Loader {
        margin[3] = size
        return this
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun margin(left: Int, top: Int, right: Int, bottom: Int): Loader {
        margin(left, top, right, bottom)
        return this
    }

    fun setOnLoaderClickListener(block: OnLoaderClickBlock): Loader {
        this.block = block
        return this
    }

    fun copy(): Loader {
        val bao = ByteArrayOutputStream()
        val oos: ObjectOutputStream
        var obj: Any? = null
        try {
            oos = ObjectOutputStream(bao)
            oos.writeObject(this)
            oos.close()
            val bis = ByteArrayInputStream(bao.toByteArray())
            val ois = ObjectInputStream(bis)
            obj = ois.readObject()
            ois.close()
        } catch (e: Exception) {
            Log.i("yhy", "e = $e")
            e.printStackTrace()
        }
        return obj as Loader
    }

    private fun setText(container: View, viewId: Int, text: String) {
        val textView: View? = container.findViewById(viewId)
        if (textView is TextView) textView.text = text
    }

    private fun setImage(container: View, viewId: Int, imageRes: Int) {
        val imageView: View? = container.findViewById(viewId)
        if (imageView is ImageView) imageView.setImageResource(imageRes)
    }


    open class Builder internal constructor() {
        private var typeMap = hashMapOf<Type, ALoadStatus>()

        fun empty(loaderStatus: AEmptyStatus): Builder {
            typeMap[loaderStatus.type] = loaderStatus
            return this@Builder
        }

        fun error(loaderStatus: AErrorStatus): Builder {
            typeMap[loaderStatus.type] = loaderStatus
            return this@Builder
        }

        fun loading(loaderStatus: ALoadingStatus): Builder {
            typeMap[loaderStatus.type] = loaderStatus
            return this@Builder
        }

        fun success(loaderStatus: ASuccessStatus): Builder {
            typeMap[loaderStatus.type] = loaderStatus
            return this@Builder
        }

        fun build(): Loader {
            val loader = Loader()
            loader.typeMap = typeMap
            return loader
        }
    }


    companion object {
        fun new(): Builder {
            return Builder()
        }
    }


    /**
     * Loader的状态
     */
    enum class Type {
        Loading,
        Empty,
        Error,
        Success,
        Custom
    }
}