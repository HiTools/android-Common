/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.hitools.common.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView


import dev.hitools.common.R


open class ScrollViewPro @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ScrollView(context, attrs, defStyleAttr) {
    private var maxHeight: Int = 0
    private var maxWidth: Int = 0

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ScrollViewPro)
        maxHeight = a.getDimensionPixelSize(R.styleable.ScrollViewPro_maxHeight, -1)
        maxWidth = a.getDimensionPixelSize(R.styleable.ScrollViewPro_maxWidth, -1)
        a.recycle()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSpec = if (maxWidth == -1) widthMeasureSpec else getAtMostSpec(maxWidth)
        val heightSpec = if (maxHeight == -1) heightMeasureSpec else getAtMostSpec(maxHeight)
        super.onMeasure(widthSpec, heightSpec)
    }


    private fun getAtMostSpec(size: Int): Int {
        return MeasureSpec.makeMeasureSpec(size, MeasureSpec.AT_MOST)
    }

    fun setMaxHeight(height: Int) {
        maxHeight = height
        requestLayout()
    }

    fun setMaxWidth(size: Int) {
        maxWidth = size
        requestLayout()
    }
}
