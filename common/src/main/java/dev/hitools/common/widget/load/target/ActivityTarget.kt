package dev.hitools.common.widget.load.target

import android.app.Activity
import android.view.ViewGroup
import dev.hitools.common.widget.load.LoadLayout
import dev.hitools.common.widget.load.Loader

/**
 * ActivityTarget
 */
class ActivityTarget(private val activity: Activity) : ITarget {

    override fun replace(loader: Loader): LoadLayout {
        val contentParent: ViewGroup = activity.findViewById(android.R.id.content)
        val childIndex = 0
        val oldContent = contentParent.getChildAt(childIndex)
        contentParent.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(oldContent.context)
        loadLayout.addView(oldContent)
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}